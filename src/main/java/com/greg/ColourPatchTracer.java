package com.greg;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ColourPatchTracer {
    private static final String BORDER_COLOUR = "00FF00";
    static final int BORDER_COLOUR_RGB = new Color(0, 0, 0).getRGB();

    /**
     * Finds the outline of a colour patch and traces the outline in BORDER_COLOUR
     * @param pixel The first pixel
     * @param inputImage The original image
     * @param outputImage The image to add an outline to
     */
    public static ColourPatch trace(Pixel pixel, BufferedImage inputImage, BufferedImage outputImage) {
        List<Pixel> path = new ArrayList<>();
        PixelMoveType moveType = PixelMoveType.RIGHT;

        while (path.size() <= 1 || !Pixel.haveSameLocation(path.get(0), pixel)) {
            if (pixel.getHexColour() != null) {
                pixel.calculateHex(inputImage);
            }

            path.add(pixel);

            outputImage.setRGB(pixel.getX(), pixel.getY(), BORDER_COLOUR_RGB);

            System.out.println(Main.pixelsScanned++);

            Optional<Pixel> nextPixel = getNextPixel(moveType, pixel, inputImage, path);

            if (nextPixel.isPresent()) {
                pixel = nextPixel.get();
            } else {
                return new ColourPatch(path, true);
            }
        }

        return new ColourPatch(path, true);
    }

    private static Optional<Pixel> getNextPixel(PixelMoveType moveType, Pixel previousPixel, BufferedImage image, List<Pixel> path) {
        moveType = moveType.getFirstAttemptMove();

        for (int moveIndex = 0; moveIndex < PixelMoveType.values().length; moveIndex++) {
            Pixel potentialNextPixel = moveType.movePixel(previousPixel);

            if (isJoinedPixel(previousPixel, potentialNextPixel, image) && !path.contains(potentialNextPixel)) {
                return Optional.of(potentialNextPixel);
            } else {
                moveType = moveType.getNextMoveType();
            }
        }

        return Optional.empty();
    }

    private static boolean pixelListBeginsToRepeat(Pixel pixel, List<Pixel> path) {
        List<Pixel> previousPixels = path.subList(path.size() - Math.min(path.size(), 10), path.size() - 1);

        return previousPixels.contains(pixel);
    }

    private static boolean isJoinedPixel(Pixel lastPixel, Pixel nextPixel, BufferedImage image) {
        Rectangle bounds = image.getData().getBounds();
        boolean isInBounds = isInBounds(nextPixel.getX(), bounds.getWidth()) && isInBounds(nextPixel.getY(), bounds.getHeight());

        if (isInBounds) {
            nextPixel.calculateHex(image);
            return lastPixel.getHexColour().equals(nextPixel.getHexColour());
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

    public static <T> T getNthLastItemFromList(List<T> list, int n) {
        int listN = n + 1;

        if(list.size() < listN) return null;

        return list.get(list.size() - listN);
    }
}
