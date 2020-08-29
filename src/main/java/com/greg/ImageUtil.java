package com.greg;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageUtil {
    private ImageUtil() {}

    public static byte[] getBytesFromImage(BufferedImage outputImage) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(outputImage, "png", baos);
        return baos.toByteArray();
    }

    public static ColourPatch convert(BufferedImage image) {
        Rectangle bounds = image.getData().getBounds();

        List<Pixel> path = new ArrayList<>();

        for (int x = 0; x < bounds.getWidth(); x++) {
            for (int y = 0; y < bounds.getHeight(); y++) {
                path.add(new Pixel(x, y));
            }
        }

        return new ColourPatch(path);
    }
}
