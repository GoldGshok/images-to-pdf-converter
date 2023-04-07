package com.goldgshok.converter.controller;

import com.goldgshok.converter.service.ConverterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ConverterController.class)
class ConverterControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private ConverterService converterService;

    @Test
    void convert() {
    }
}