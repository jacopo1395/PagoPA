package org.jacopocarlini.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jacopocarlini.constants.AppConstants;
import org.jacopocarlini.model.Message;
import org.jacopocarlini.model.SubmitMessageRequest;
import org.jacopocarlini.model.io.MessageResponse;
import org.jacopocarlini.service.MessageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ErrorHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;


    @Test
    void handleHttpMessageNotReadable() throws Exception {
        MvcResult callToTest = getMvcResultMessageNotRedable();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), callToTest.getResponse().getStatus());
    }

    @Test
    void handleMethodArgumentNotValid() throws Exception {
        SubmitMessageRequest request = SubmitMessageRequest.builder()
                .fiscalCode(null)
                .message(Message.builder()
                        .subject("Welcome new user !")
                        .markdown("# This is a markdown header\\n\\nto show how easily markdown can be converted to **HTML**\\n\\nRemember: this has to be a long text.")
                        .build())
                .build();
        MvcResult callToTest = getMvcResult(request);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), callToTest.getResponse().getStatus());
    }

    @Test
    void handleIOException() throws Exception {
        SubmitMessageRequest request = SubmitMessageRequest.builder()
                .fiscalCode("AAAAAA00A00A000A")
                .message(Message.builder()
                        .subject("Welcome new user !")
                        .markdown("# This is a markdown header\\n\\nto show how easily markdown can be converted to **HTML**\\n\\nRemember: this has to be a long text.")
                        .build())
                .build();
        Mockito.when(messageService.submitMessageForUser(any(SubmitMessageRequest.class)))
                .thenThrow(new IOCallException("details", HttpStatus.BAD_REQUEST));

        MvcResult callToTest = getMvcResult(request);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), callToTest.getResponse().getStatus());

    }

    @Test
    void handleAppException() throws Exception {
        SubmitMessageRequest request = SubmitMessageRequest.builder()
                .fiscalCode("AAAAAA00A00A000A")
                .message(Message.builder()
                        .subject("Welcome new user !")
                        .markdown("# This is a markdown header\\n\\nto show how easily markdown can be converted to **HTML**\\n\\nRemember: this has to be a long text.")
                        .build())
                .build();
        Mockito.when(messageService.submitMessageForUser(any(SubmitMessageRequest.class)))
                .thenThrow(new AppException("details", HttpStatus.BAD_REQUEST));

        MvcResult callToTest = getMvcResult(request);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), callToTest.getResponse().getStatus());
    }


    @Test
    void handleGenericException() throws Exception {
        SubmitMessageRequest request = SubmitMessageRequest.builder()
                .fiscalCode("AAAAAA00A00A000A")
                .message(Message.builder()
                        .subject("Welcome new user !")
                        .markdown("# This is a markdown header\\n\\nto show how easily markdown can be converted to **HTML**\\n\\nRemember: this has to be a long text.")
                        .build())
                .build();
        Mockito.when(messageService.submitMessageForUser(any(SubmitMessageRequest.class)))
                .thenThrow(new RuntimeException());

        MvcResult callToTest = getMvcResult(request);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), callToTest.getResponse().getStatus());


    }

    private MvcResult getMvcResultMessageNotRedable() throws Exception {
        String requestJson = "{sdfsd}";
        return mockMvc
                .perform(MockMvcRequestBuilders.request(HttpMethod.POST, AppConstants.Url.SUBMIT_MESSAGE_FOR_USER)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                )
                .andReturn();
    }

    private MvcResult getMvcResult(Object request) throws Exception {
        String requestJson = new ObjectMapper().writeValueAsString(request);
        return mockMvc
                .perform(MockMvcRequestBuilders.request(HttpMethod.POST, AppConstants.Url.SUBMIT_MESSAGE_FOR_USER)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                )
                .andReturn();
    }

}
