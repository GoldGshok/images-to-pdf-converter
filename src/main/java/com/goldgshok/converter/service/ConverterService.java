package com.goldgshok.converter.service;

import com.goldgshok.converter.request.ConvertRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

@Slf4j
@Service
public class ConverterService {

	public void convert(ConvertRequest request) {
		File file = new File(request.getInputFolderPath());


	}

}
