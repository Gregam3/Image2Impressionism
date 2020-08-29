package com.greg;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ColourPatchTracer {
    public static final String BORDER_COLOUR = "FF0000";

    /**
     * Finds the outline of a colour patch and traces the outline in BORDER_COLOUR
     * @param pixel The first pixel
     * @param inputImage The original image
     * @param outputImage The image to add an outline to
     */
    public static ColourPatch trace(Pixel pixel, BufferedImage inputImage, BufferedImage outputImage) {
        List<Pixel> path = new ArrayList<>();
        PixelMoveType moveType = PixelMoveType.RIGHT;
        Rectangle imageBounds = inputImage.getData().getBounds();

        while (path.size() <= 1 || !Pixel.haveSameLocation(path.get(0), pixel)) {
            if (pixel.getHex() != null) {
                pixel.calculateHex(inputImage);
            }

            System.out.println(pixel.toString());
            outputImage.setRGB(pixel.getX(), pixel.getY(), toRgbInt(BORDER_COLOUR));

            path.add(pixel);

            moveType = moveType.getFirstAttemptMove();

            for (int i = 0; i < PixelMoveType.values().length; i++) {
                Pixel potentialNextPixel = moveType.movePixel(pixel);

                if (isJoinedPixel(pixel, potentialNextPixel, imageBounds, inputImage)) {
                    pixel = potentialNextPixel;
                    break;
                } else {
                    moveType = moveType.getNextMoveType();
                }
            }

            if (path.get(path.size() - 1).equals(pixel)) {
                System.out.println("Finished");
                break;
            }
        }

        return new ColourPatch(path);
    }

    private static boolean isJoinedPixel(Pixel lastPixel, Pixel nextPixel, Rectangle bounds, BufferedImage inputImage) {
        boolean isInBounds = isInBounds(nextPixel.getX(), bounds.getWidth()) && isInBounds(nextPixel.getY(), bounds.getHeight());

        if (isInBounds) {
            nextPixel.calculateHex(inputImage);
            return lastPixel.getHex().equals(nextPixel.getHex());
        } else {
            return false;
        }
    }

    private static boolean isInBounds(int num, double max) {
        return num > 0 && num < max;
    }

    public static int toRgbInt(String hex) {
        return Color.decode("#" + hex).getRGB();
    }
}
