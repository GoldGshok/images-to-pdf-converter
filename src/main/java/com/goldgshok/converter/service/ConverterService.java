package com.goldgshok.converter.service;

import com.goldgshok.converter.request.ConvertRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ConverterService {

    public void convert(ConvertRequest request) {
        var file = new File(request.getInputFolderPath());
        var dirs = file.listFiles(File::isDirectory);

        if (dirs != null && dirs.length > 0) {
            for (var dir : dirs) {
                createPdf(dir, request.getOutputFolderPath());
            }
        } else {
            createPdf(file, request.getOutputFolderPath());
        }
    }

    private void createPdf(File directory, String outputPath) {
        var path = directory.getPath() + File.separator;
        List<String> imageNames = getImageNames(directory);
        if (!imageNames.isEmpty()) {
            try (var document = new PDDocument()) {
                for (var imageName : imageNames) {
                    fillPage(document, path, imageName);
                }
                var fileName = String.format("%s%s.pdf", outputPath, directory.getName());
                document.save(fileName);
                log.debug("File {} created", fileName);
            } catch (Exception e) {
                log.error("Error generation pdf for {}", directory.getName());
            }
        }
    }

    @SneakyThrows
    private void fillPage(PDDocument document, String path, String imageName) {
        var file = new File(path + imageName);
        var imageInfo = Sanselan.getImageInfo(file);
        var width = imageInfo.getWidth();
        var height = imageInfo.getHeight();
        var isHorizontal = width > height;

        var page = getPage(isHorizontal);
        document.addPage(page);

        Dimension pageDim;
        if (isHorizontal) {
            pageDim = new Dimension((int) PDRectangle.A4.getHeight(), (int) PDRectangle.A4.getWidth());
        } else {
            pageDim = new Dimension((int) PDRectangle.A4.getWidth(), (int) PDRectangle.A4.getHeight());
        }
        var imageDim = new Dimension(width, height);

        var scaledDim = getScaledDimension(imageDim, pageDim);

        var image = PDImageXObject.createFromFileByContent(file, document);
        var contentStream = new PDPageContentStream(document, page);
        contentStream.drawImage(image, 0, 0, scaledDim.width, scaledDim.height);
        contentStream.close();
    }

    private List<String> getImageNames(File directory) {
        String[] images = directory.list((dir, name) -> isImageFile(name));
        List<String> sortedImages = Collections.emptyList();
        if (images != null) {
            sortedImages = Arrays.stream(images)
                    .sorted(this::compareImageNames)
                    .toList();
        }
        return sortedImages;
    }

    private PDPage getPage(boolean isHorizontal) {
        PDRectangle layout;
        if (isHorizontal) {
            layout = new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth());
        } else {
            layout = PDRectangle.A4;
        }
        return new PDPage(layout);
    }

    public int compareImageNames(String o1, String o2) {
        var diff = o1.length() - o2.length();
        if (diff == 0) {
            return o1.compareTo(o2);
        } else {
            return diff;
        }
    }

    public static Dimension getScaledDimension(Dimension imgSize, Dimension page) {
        var originalWidth = imgSize.width;
        var originalHeight = imgSize.height;
        var pageWidth = page.width;
        var pageHeight = page.height;

        var newWidth = originalWidth;
        var newHeight = originalHeight;

        if (originalWidth > pageWidth) {
            newWidth = pageWidth;
            newHeight = (newWidth * originalHeight) / originalWidth;
        }
        if (newHeight > pageHeight) {
            newHeight = pageHeight;
            newWidth = (newHeight * originalWidth) / originalHeight;
        }
        return new Dimension(newWidth, newHeight);
    }

    private boolean isImageFile(String fileName) {
        return fileName.endsWith(".png")
                || fileName.endsWith(".jpg")
                || fileName.endsWith(".jpeg")
                || fileName.endsWith(".bmp");
    }
}
