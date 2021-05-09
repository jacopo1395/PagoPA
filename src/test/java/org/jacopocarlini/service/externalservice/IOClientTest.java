package org.jacopocarlini.service.externalservice;

import com.google.common.io.Resources;
import org.jacopocarlini.constants.AppConstants;
import org.jacopocarlini.exception.IOCallException;
import org.jacopocarlini.model.Message;
import org.jacopocarlini.model.io.MessageResponse;
import org.jacopocarlini.model.io.ProfileResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class IOClientTest {

    public static final String FISCAL_CODE = "AAAAAA00A00A000A";
    public static final String SUBJECT = "Welcome new user !";
    public static final String MARKDOWN = "# This is a markdown header\\n\\nto show how easily markdown can be converted to **HTML**\\n\\nRemember: this has to be a long text.";
    public static final String GET_PROFILE_URL = "https://api.io.italia.it/api/v1/profiles/AAAAAA00A00A000A";
    private static final String SUBMIT_MESSAGE_URL = "https://api.io.italia.it/api/v1/messages";
    @Value(value = "${io_api_key}")
    String ioApiKey;
    private MockRestServiceServer mockIOServer;
    @Autowired
    private IORestTemplate ioRestTemplate;
    @Autowired
    private IOClient ioClient;

    @BeforeEach
    void setUp() {
        mockIOServer = MockRestServiceServer.bindTo(ioRestTemplate).ignoreExpectOrder(true).build();
    }

    @Test
    void submitMessageForUserOK() throws IOException {
        mockSubmitMessageCall();
        MessageResponse response = ioClient.submitMessageForUser(FISCAL_CODE, Message.builder().subject(SUBJECT).markdown(MARKDOWN).build());

        MessageResponse expected = MessageResponse.builder().messageId("1").build();
        assertEquals(expected, response);
    }

    @Test
    void getProfileOK() throws IOException {
        mockProfileCall(HttpStatus.OK, "ioresponse/profileResponse.json");
        ProfileResponse response = ioClient.getProfile(FISCAL_CODE);

        ProfileResponse expected = ProfileResponse.builder().senderAllowed(Boolean.TRUE).build();
        assertEquals(expected, response);
    }

    @Test
    void getProfileKO() throws IOException {
        try {
            mockProfileCall(HttpStatus.NOT_FOUND, "ioresponse/errorResponse.json");
            ioClient.getProfile(FISCAL_CODE);
            fail();
        } catch (IOCallException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
        } catch (Exception e) {
            fail();
        }
    }

    private void mockSubmitMessageCall() throws IOException {
        mockIOServer.expect(requestTo(SUBMIT_MESSAGE_URL))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(resourcesToJson("iorequest/messageRequest.json")))
                .andExpect(header(AppConstants.IOUrl.OCP_APIM_SUBSCRIPTION_KEY, ioApiKey))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(resourcesToJson("ioresponse/messageResponse.json")));
    }

    private void mockProfileCall(HttpStatus httpStatus, String resourceName) throws IOException {
        mockIOServer.expect(requestTo(GET_PROFILE_URL))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(AppConstants.IOUrl.OCP_APIM_SUBSCRIPTION_KEY, ioApiKey))
                .andRespond(withStatus(httpStatus)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(resourcesToJson(resourceName)));
    }

    private String resourcesToJson(String resourceName) throws IOException {
        return Resources.toString(Resources.getResource(resourceName), Charset.defaultCharset());
    }


}
