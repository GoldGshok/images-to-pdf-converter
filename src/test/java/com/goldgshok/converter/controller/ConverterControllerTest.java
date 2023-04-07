package com.goldgshok.converter.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goldgshok.converter.request.ConvertRequest;
import com.goldgshok.converter.service.ConverterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConverterController.class)
class ConverterControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ConverterService converterService;

    @Test
    void convert_baseCase_success() throws Exception {
        var request = new ConvertRequest();

        var body = objectMapper.writeValueAsString(request);

        var mockRequest = MockMvcRequestBuilders.post("/api/converter/convert")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body);

        mvc.perform(mockRequest)
                .andExpect(status().isOk());

        verify(converterService).convert(any());
    }
}