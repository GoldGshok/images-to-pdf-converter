package com.goldgshok.converter.controller;

import com.goldgshok.converter.BaseControllerTest;
import com.goldgshok.converter.request.ConvertRequest;
import com.goldgshok.converter.service.ConverterService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

class ConverterControllerTest extends BaseControllerTest {

    @MockBean
    private ConverterService converterService;

    @Test
    void convert_baseCase_success() {
        var request = new ConvertRequest();
        request.setInputFolderPath("123");
        request.setInputFolderPath("123");

        makeRequest("/api/converter/convert", request);

        verify(converterService).convert(any());
    }
}