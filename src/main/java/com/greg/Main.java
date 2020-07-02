package com.greg;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static BufferedImage image;
    private static Rectangle bounds;

    public static void main(String[] args) {
        final File imageFile = new File("src/main/resources/test1.png");
        try {
            image = ImageIO.read(imageFile);
            bounds = image.getData().getBounds();

            printJoinedPixels(new Pixel(0, 0), new ArrayList<>());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printJoinedPixels(Pixel pixel, List<Pixel> path) {
        pixel.setHex(image);
        System.out.println(pixel.toString());

        if (!path.isEmpty() && path.get(0) == pixel) {
            return;
        }

        path.add(pixel);

        for (PixelMoveType pixelMoveType : PixelMoveType.values()) {
            Pixel potentialNextPixel = pixelMoveType.movePixel(pixel);
            if (isJoinedPixel(pixel, potentialNextPixel)) {
                printJoinedPixels(potentialNextPixel, path);
                return;
            }
        }
    }

    private static boolean isJoinedPixel(Pixel lastPixel, Pixel nextPixel) {
        boolean isInBounds = isInBounds(nextPixel.getX(), bounds.getWidth()) && isInBounds(nextPixel.getY(), bounds.getHeight());

        if (isInBounds) {
            nextPixel.setHex(image);
            return lastPixel.getHex().equals(nextPixel.getHex());
        } else {
            return false;
        }
    }

    private static boolean isInBounds(int num, double max) {
        return num >= 0 && num < max;
    }
}
