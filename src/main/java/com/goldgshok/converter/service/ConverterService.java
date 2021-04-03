package com.goldgshok.converter.service;

import com.goldgshok.converter.request.ConvertRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.Sanselan;
import org.springframework.boot.convert.Delimiter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ConverterService {

	public void convert(ConvertRequest request) {
		File file = new File(request.getInputFolderPath());
		File[] dirs = file.listFiles(File::isDirectory);

		if (dirs != null && dirs.length > 0) {
			for (File dir : dirs) {
				createPdf(dir, request.getOutputFolderPath());
			}
		} else {
			createPdf(file, request.getOutputFolderPath());
		}
	}

	private void createPdf(File directory, String outputPath) {
		String path = directory.getPath() + File.separator;
		List<String> imageNames = getImageNames(directory);
		if (!imageNames.isEmpty()) {
			try (PDDocument document = new PDDocument()) {
				for (String imageName : imageNames) {
					File file = new File(path + imageName);
					PDPage page = getPage(file);
					document.addPage(page);

					PDImageXObject image = PDImageXObject.createFromFileByContent(file, document);
					PDPageContentStream contentStream = new PDPageContentStream(document, page);
					contentStream.drawImage(image, 0, 0);
					contentStream.close();
				}
				String fileName = String.format("%s%s.pdf", outputPath, directory.getName());
				document.save(fileName);
			} catch (Exception e) {
				log.error("Error generation pdf for {}", directory.getName());
			}
		}
	}

	private List<String> getImageNames(File directory) {
		String[] images = directory.list((dir, name) -> name.endsWith(".png")
				|| name.endsWith(".jpg")
				|| name.endsWith(".jpeg")
				|| name.endsWith(".bmp"));
		List<String> sortedImages = Collections.emptyList();
		if (images != null) {
			sortedImages = Arrays.stream(images)
					.sorted(this::compareImageNames)
					.collect(Collectors.toList());
		}
		return sortedImages;
	}

	private PDPage getPage(File file) throws Exception {
		ImageInfo imageInfo = Sanselan.getImageInfo(file);
		int width = imageInfo.getWidth();
		int height = imageInfo.getHeight();
		PDRectangle layout;
		if (width > height) {
			layout = new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth());
		} else {
			layout = PDRectangle.A4;
		}
		return new PDPage(layout);
	}

	public int compareImageNames(String o1, String o2) {
		int diff = o1.length() - o2.length();
		if (diff == 0) {
			return o1.compareTo(o2);
		} else {
			return diff;
		}
	}
}
