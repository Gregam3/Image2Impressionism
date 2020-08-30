package com.greg;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ColourPatchTracer {
    private static final String BORDER_COLOUR = "FF0000";
    private static final int BORDER_COLOUR_RGB = toRgbInt(BORDER_COLOUR);

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

            System.out.println(pixel.toString());
            outputImage.setRGB(pixel.getX(), pixel.getY(), BORDER_COLOUR_RGB);

            path.add(pixel);

            Optional<Pixel> nextPixel = getNextPixel(moveType, pixel, inputImage);

            if (nextPixel.isPresent()) {
                pixel = nextPixel.get();
            }

            if (!nextPixel.isPresent() || pixelListBeginsToRepeat(pixel, path)) {
                System.out.println("Finished tracing");
                return new ColourPatch(path);
            }
        }

        throw new AssertionError("This state should never occur, tracing did not reach termination criteria");
    }

    private static Optional<Pixel> getNextPixel(PixelMoveType moveType, Pixel previousPixel, BufferedImage image) {
        moveType = moveType.getFirstAttemptMove();

        for (int moveIndex = 0; moveIndex < PixelMoveType.values().length; moveIndex++) {
            Pixel potentialNextPixel = moveType.movePixel(previousPixel);

            if (isJoinedPixel(previousPixel, potentialNextPixel, image)) {
                return Optional.of(potentialNextPixel);
            } else {
                moveType = moveType.getNextMoveType();
            }
        }

        return Optional.empty();
    }

    private static boolean pixelListBeginsToRepeat(Pixel pixel, List<Pixel> path) {
        return path.get(path.size() - 1).equals(pixel);
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
}
