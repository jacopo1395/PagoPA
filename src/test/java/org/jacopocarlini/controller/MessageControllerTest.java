package org.jacopocarlini.controller;

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

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @BeforeEach
    public void setUp() {
        Mockito.when(messageService.submitMessageForUser(any(SubmitMessageRequest.class)))
                .thenReturn(new MessageResponse());
    }

    @Test
    void submitMessageForUserOK() throws Exception {
        SubmitMessageRequest request = SubmitMessageRequest.builder()
                .fiscalCode("AAAAAA00A00A000A")
                .message(Message.builder()
                        .subject("Welcome new user !")
                        .markdown("# This is a markdown header\\n\\nto show how easily markdown can be converted to **HTML**\\n\\nRemember: this has to be a long text.")
                        .build())
                .build();
        MvcResult callToTest = getMvcResult(request);
        Assertions.assertEquals(HttpStatus.CREATED.value(), callToTest.getResponse().getStatus());
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
