package com.goldgshok.converter.service;

import com.goldgshok.converter.request.ConvertRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class ConverterService {

	public void convert(ConvertRequest request) {
		File file = new File(request.getInputFolderPath());
		File[] dirs = file.listFiles(this::filterDirs);

		String extension = request.getExtension();
		if (dirs != null) {
			for (File dir : dirs) {
				createPdf(dir, request.getOutputFolderPath(), extension);
			}
		} else {
			createPdf(file, request.getOutputFolderPath(), extension);
		}
	}

	private boolean filterDirs(File file) {
		return file.isDirectory();
	}

	private void createPdf(File directory, String outputPath, String extension) {
		String[] images = directory.list((dir, name) -> name.endsWith(extension));

		if (images != null) {
			try (PDDocument document = new PDDocument()) {
				for (String imagePath : images) {
					File file = new File(imagePath);
					BufferedImage bimg = ImageIO.read(file);
					int width = bimg.getWidth();
					int height = bimg.getHeight();
					PDRectangle layout;
					if (width > height) {
						layout = new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth());
					} else {
						layout = PDRectangle.A4;
					}
					PDPage page = new PDPage(layout);

					document.addPage(page);

					Path path = Paths.get(file.toURI());
					PDPageContentStream contentStream = new PDPageContentStream(document, page);
					PDImageXObject image
							= PDImageXObject.createFromFile(path.toAbsolutePath().toString(), document);
					contentStream.drawImage(image, 0, 0);
					contentStream.close();
				}
				document.save(outputPath + directory.getName());
			} catch (Exception e) {
				log.error("Error generation pdf for {}", directory.getName());
			}
		}
	}

}
