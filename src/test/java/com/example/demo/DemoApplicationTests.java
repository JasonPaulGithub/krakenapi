package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RequestController.class)
class WebMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestService service;

    @Test
    void greetingShouldReturnMessageFromService() throws Exception {

        String input = "Hello";

        when(service.greet(input)).thenReturn(input);

        this.mockMvc.perform(post("/request/Hello"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(input)));
    }

    //TODO: error handling and exceptions
}
