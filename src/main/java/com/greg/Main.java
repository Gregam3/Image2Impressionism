package com.greg;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static BufferedImage elephantImage;
    private static Rectangle bounds;

    public static void main(String[] args) {
        final File imageFile = new File("PaintingStepCalculator/src/main/resources/monkey.png");
        try {
            elephantImage = ImageIO.read(imageFile);
            bounds = elephantImage.getData().getBounds();

            for (int x = 0; x < bounds.getWidth(); x++) {
                for (int y = 0; y < bounds.getHeight(); y++) {
                    printJoinedPixels(new Pixel(x, y, elephantImage), new ArrayList<>());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printJoinedPixels(Pixel pixel, List<Pixel> path) {
        System.out.println(pixel.toString());
        path.add(pixel);

        if (!path.isEmpty() && path.get(0) == pixel) {
            return;
        }

        for (PixelMoveType pixelMoveType : PixelMoveType.values()) {
            Pixel potentialNextPixel = pixelMoveType.movePixel(elephantImage, pixel);
            if(isJoinedPixel(pixel, potentialNextPixel)) {
                printJoinedPixels(potentialNextPixel, path);
                return;
            }
        }
    }

    private static boolean isJoinedPixel(Pixel lastPixel, Pixel nextPixel) {
        boolean isInBounds = isInBounds(nextPixel.getX(), bounds.getHeight()) && isInBounds(nextPixel.getY(), bounds.getWidth());

        return isInBounds && lastPixel.getHex().equals(nextPixel.getHex());
    }

    private static boolean isInBounds(int num, double max) {
        return num > 0 && num < max;
    }
}
