package org.kgromov.drugstore.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.util.DigestUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageUtils {

    // FileSystemResource
    @SneakyThrows
    public static File resizeImage(InputStream source, String destImgPath, int targetWidth, int targetHeight) {
        BufferedImage originalImage = ImageIO.read(source);
        return resizeImage(originalImage, destImgPath, targetWidth, targetHeight);
    }

    @SneakyThrows
    public static File resizeImage(String sourceImgPath, String destImgPath, int targetWidth, int targetHeight) {
        BufferedImage originalImage = ImageIO.read(new File(sourceImgPath));
        return resizeImage(originalImage, destImgPath, targetWidth, targetHeight);
    }

    @SneakyThrows
    public static File resizeImage(String sourceImgPath, String destImgPath, float scaleFactor) {
        BufferedImage originalImage = ImageIO.read(new File(sourceImgPath));
        int targetWidth = (int) (originalImage.getWidth() * scaleFactor);
        int targetHeight = (int) (originalImage.getHeight() * scaleFactor);
        return resizeImage(originalImage, destImgPath, targetWidth, targetHeight);
    }

    @SneakyThrows
    private static File resizeImage(BufferedImage sourceImage, String destImgPath, int targetWidth, int targetHeight) {
        Image image = sourceImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage buffered = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        buffered.getGraphics().drawImage(image, 0, 0, null);
        File resultImage = new File(destImgPath);
        ImageIO.write(buffered, "png", resultImage);
        return resultImage;
    }

    @SneakyThrows
    public static String m5Digest(File file) {
        try (var inputStream = new FileInputStream(file)) {
            return DigestUtils.md5DigestAsHex(inputStream);
        }
    }

    @SneakyThrows
    public static String m5Digest(InputStream inputStream) {
        return DigestUtils.md5DigestAsHex(inputStream);
    }
}
