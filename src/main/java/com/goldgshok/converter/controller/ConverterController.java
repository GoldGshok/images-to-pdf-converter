package com.goldgshok.converter.controller;

import com.goldgshok.converter.request.ConvertRequest;
import com.goldgshok.converter.service.ConverterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/converter")
@Tag(name = "Контролер конвертера", description = "API для конвертации картинок в PDF файл")
public class ConverterController {

    private final ConverterService converter;

    @PostMapping("/convert")
    @Operation(summary = "Конвертация картинок в PDF файл")
    public void downloadImages(@RequestBody ConvertRequest request) {
        converter.convert(request);
    }

}
