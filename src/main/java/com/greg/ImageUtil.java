package com.greg;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageUtil {
    private ImageUtil() {
    }

    public static byte[] getBytesFromImage(BufferedImage outputImage) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(outputImage, "png", baos);
        return baos.toByteArray();
    }

    public static ColourPatch convert(BufferedImage inputImage, BufferedImage outputImage) {
        Rectangle bounds = inputImage.getData().getBounds();

        List<Pixel> path = new ArrayList<>();

        int x = 0;
        int y = 0;

        for (; x < bounds.getWidth() - 1; x++) path.add(new Pixel(x, y, inputImage));
        for (; y < bounds.getHeight() - 1; y++) path.add(new Pixel(x, y, inputImage));
        for (; x > 0; x--) path.add(new Pixel(x, y, inputImage));
        for (; y > 0; y--) path.add(new Pixel(x, y, inputImage));

        for (Pixel pixel : path) {
            outputImage.setRGB(pixel.getX(), pixel.getY(), ColourPatchTracer.BORDER_COLOUR_RGB);
        }

        return new ColourPatch(path);
    }
}
