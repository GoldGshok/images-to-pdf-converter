package com.goldgshok.converter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goldgshok.converter.BaseControllerTest;
import com.goldgshok.converter.SpringBootBaseTest;
import com.goldgshok.converter.request.ConvertRequest;
import com.goldgshok.converter.service.ConverterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ConverterControllerTest extends BaseControllerTest {

    @MockBean
    private ConverterService converterService;

    @Test
    void convert_baseCase_success() {
        var request = new ConvertRequest();

        makeRequest("/api/converter/convert", request);

        verify(converterService).convert(any());
    }
}