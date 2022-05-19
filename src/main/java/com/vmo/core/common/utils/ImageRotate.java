package com.vmo.core.common.utils;

import com.vmo.core.modules.models.requests.image.AngleRotateImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageRotate {
//    public static MultipartFile rotate90(File input, AngleRotateImage direction) {
//        File output = new File(System.getProperty("java.io.tmpdir") + "/" + input.getName());
//        MultipartFile fileOutput = null;
//        try {
//            ImageInputStream iis = ImageIO.createImageInputStream(input);
//            Iterator<ImageReader> iterator = ImageIO.getImageReaders(iis);
//            ImageReader reader = iterator.next();
//            String format = reader.getFormatName();
//
//            BufferedImage image = ImageIO.read(iis);
//            int width = image.getWidth();
//            int height = image.getHeight();
//
//            BufferedImage rotated = new BufferedImage(height, width, image.getType());
//
//            for (int y = 0; y < height; y++) {
//                for (int x = 0; x < width; x++) {
//                    switch (direction) {
//                        case LEFT:
//                            rotated.setRGB(y, (width - 1) - x, image.getRGB(x, y));
//                            break;
//                        case RIGHT:
//                            rotated.setRGB((height - 1) - y, x, image.getRGB(x, y));
//
//                    }
//                }
//            }
//
//            ImageIO.write(rotated, format, output);
//            FileInputStream outputFile = new FileInputStream(output);
//            fileOutput = new MockMultipartFile("outputFile", output.getName(), MediaType.IMAGE_JPEG_VALUE, IOUtils.toByteArray(outputFile));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return fileOutput;
//    }
//
//    public static MultipartFile rotate180(File input) {
//        File output = new File(System.getProperty("java.io.tmpdir") + "/" + "rotate180.jpg");
//        MultipartFile fileOutput = null;
//        try {
//            ImageInputStream iis = ImageIO.createImageInputStream(input);
//            Iterator<ImageReader> iterator = ImageIO.getImageReaders(iis);
//            ImageReader reader = iterator.next();
//            String format = reader.getFormatName();
//
//            BufferedImage image = ImageIO.read(iis);
//            int width = image.getWidth();
//            int height = image.getHeight();
//
//            BufferedImage rotated = new BufferedImage(width, height, image.getType());
//
//            for (int y = 0; y < height; y++) {
//                for (int x = 0; x < width; x++) {
//                    rotated.setRGB((width - 1) - x, (height - 1) - y, image.getRGB(x, y));
//                }
//            }
//
//            ImageIO.write(rotated, format, output);
//            FileInputStream outputFile = new FileInputStream(output);
//            fileOutput = new MockMultipartFile("outputFile", output.getName(), MediaType.IMAGE_JPEG_VALUE, IOUtils.toByteArray(outputFile));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return fileOutput;
//    }

    public static byte[] rotateImage(byte[] imageData, AngleRotateImage angle, String contentType) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(imageData);
        BufferedImage src = ImageIO.read(inputStream);
        double theta;
        int width = src.getWidth();
        int height = src.getHeight();
        BufferedImage dest;
        if (angle == AngleRotateImage.LEFT || angle == AngleRotateImage.RIGHT) {
            dest = new BufferedImage(src.getHeight(), src.getWidth(), src.getType() == 0 ? 5 : src.getType());
        } else {
            dest = new BufferedImage(src.getWidth(), src.getHeight(), src.getType() == 0 ? 5 : src.getType());
        }

        Graphics2D graphics2D = dest.createGraphics();

        switch (angle) {
            case RIGHT:
                theta = (Math.PI * 2) / 360 * 90;
                graphics2D.translate((height - width) / 2, (height - width) / 2);
                graphics2D.rotate(theta, (double) height / 2, (double) width / 2);
                break;
            case LEFT:
                theta = (Math.PI * 2) / 360 * 270;
                graphics2D.translate((width - height) / 2, (width - height) / 2);
                graphics2D.rotate(theta, (double) height / 2, (double) width / 2);
                break;
            case REVERSE:
                theta = (Math.PI * 2) / 360 * 180;
                graphics2D.translate(0, 0);
                graphics2D.rotate(theta, (double) width / 2, (double) height / 2);
                break;
        }
        graphics2D.drawRenderedImage(src, null);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(dest, contentType, byteArrayOutputStream);
        byteArrayOutputStream.flush();
        byte[] imageInByte = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return imageInByte;
    }
}
