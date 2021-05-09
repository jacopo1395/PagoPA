package org.jacopocarlini.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jacopocarlini.exception.AppException;
import org.jacopocarlini.model.Message;
import org.jacopocarlini.model.SubmitMessageRequest;
import org.jacopocarlini.model.io.MessageResponse;
import org.jacopocarlini.model.io.ProfileResponse;
import org.jacopocarlini.service.externalservice.IOClient;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.charset.Charset;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class MessageServiceImplTest {

    @Mock
    IOClient ioClient;

    @Autowired
    @InjectMocks
    MessageServiceImpl messageService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void submitMessageForUser() throws JSONException, IOException {
        String fiscalCode = "AAAAAA00A00A000A";
        Message message = Message.builder()
                .subject("Welcome new user !")
                .markdown("# This is a markdown header\\n\\nto show how easily markdown can be converted to **HTML**\\n\\nRemember: this has to be a long text.")
                .build();
        when(ioClient.getProfile(fiscalCode)).thenReturn(ProfileResponse.builder().senderAllowed(Boolean.TRUE).build());
        when(ioClient.submitMessageForUser(fiscalCode, message)).thenReturn(MessageResponse.builder().messageId("1").build());
        SubmitMessageRequest submitMessageRequest = SubmitMessageRequest.builder()
                .fiscalCode(fiscalCode)
                .message(message)
                .build();
        MessageResponse response = messageService.submitMessageForUser(submitMessageRequest);
        String actualResp = new ObjectMapper().writeValueAsString(response);
        String expectedResp = Resources.toString(Resources.getResource("response/submitMessageForUser.json"), Charset.defaultCharset());
        JSONAssert.assertEquals(expectedResp, actualResp, JSONCompareMode.NON_EXTENSIBLE);
    }

    @Test
    void submitMessageForUserSenderNotAllowed() throws JSONException, IOException {
        try {
            String fiscalCode = "AAAAAA00A00A000A";
            Message message = Message.builder()
                    .subject("Welcome new user !")
                    .markdown("# This is a markdown header\\n\\nto show how easily markdown can be converted to **HTML**\\n\\nRemember: this has to be a long text.")
                    .build();
            when(ioClient.getProfile(fiscalCode)).thenReturn(ProfileResponse.builder().senderAllowed(Boolean.FALSE).build());
            SubmitMessageRequest submitMessageRequest = SubmitMessageRequest.builder()
                    .fiscalCode(fiscalCode)
                    .message(message)
                    .build();
            messageService.submitMessageForUser(submitMessageRequest);
            fail();
        }
        catch (AppException e){
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY ,e.getHttpStatus());
        }
        catch (Exception e){
            fail();
        }
    }
}
