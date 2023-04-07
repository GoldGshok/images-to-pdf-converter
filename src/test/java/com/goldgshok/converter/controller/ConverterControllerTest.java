package com.goldgshok.converter.controller;

import com.goldgshok.converter.BaseControllerTest;
import com.goldgshok.converter.request.ConvertRequest;
import com.goldgshok.converter.service.ConverterService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ConverterControllerTest extends BaseControllerTest {

    @MockBean
    private ConverterService converterService;

    @Test
    void convert_baseCase_success() {
        var request = new ConvertRequest();
        request.setInputFolderPath("123");
        request.setOutputFolderPath("456");

        makeRequest("/api/converter/convert", request);

        verify(converterService).convert(any());
    }

    @Test
    void convert_emptyRequest_throwException() throws Exception {
        var request = new ConvertRequest();
        request.setInputFolderPath("123");
        request.setOutputFolderPath("456");

        var result = makeRequest("/api/converter/convert", null);
        result.andExpect(status().is4xxClientError());

        verifyNoInteractions(converterService);
    }
}