package com.goldgshok.converter.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ConvertRequest {
	@Schema(description = "Начальная страница для поиска картинок", example = "D://Books//", required = true)
	private String inputFolderPath;
	@Schema(description = "Путь для сохранения pdf-файлов", example = "D://Books//", required = true)
	private String outputFolderPath;

}
