package com.greg;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ColourPatchTracer {
    static final int BORDER_COLOUR_RGB = new Color(255, 0, 0).getRGB();
    static final String BORDER_COLOUR_HEX = Integer.toHexString(BORDER_COLOUR_RGB);

    /**
     * Finds the outline of a colour patch and traces the outline in BORDER_COLOUR
     *
     * @param pixel       The first pixel
     * @param inputImage  The original image
     * @param outputImage The image to add an outline to
     * @param parentPatch
     */
    public static ColourPatch trace(Pixel pixel, BufferedImage inputImage, BufferedImage outputImage, ColourPatch parentPatch) {
        List<Pixel> path = new ArrayList<>();
        PixelMoveType moveType = PixelMoveType.RIGHT;

        while (path.size() <= 1 || !Pixel.haveSameLocation(path.get(0), pixel)) {
            if (pixel.getHexColour() != null) {
                pixel.calculateHex(inputImage);
            }

//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            path.add(pixel);

            outputImage.setRGB(pixel.getX(), pixel.getY(), BORDER_COLOUR_RGB);

            System.out.println(ImageBoundaryDrawer.pixelsScanned++);

            Optional<Pixel> nextPixel = getNextPixel(moveType, pixel, inputImage, parentPatch);

            if (nextPixel.isPresent()) {
                pixel = nextPixel.get();
                moveType = pixel.getCameFrom();
            } else {
                return new ColourPatch(path);
            }
        }

        return new ColourPatch(path);
    }

    private static Optional<Pixel> getNextPixel(PixelMoveType moveType, Pixel previousPixel, BufferedImage image, ColourPatch parentPatch) {
        moveType = moveType.getFirstAttemptMove();

        for (int moveIndex = 0; moveIndex < PixelMoveType.values().length; moveIndex++) {
            Pixel potentialNextPixel = moveType.movePixel(previousPixel);
            if (isInBounds(potentialNextPixel, image)) {
                potentialNextPixel.calculateHex(image);

                if (shouldMoveTo(moveType, previousPixel, potentialNextPixel)) {
                    potentialNextPixel.setCameFrom(moveType);
                    return Optional.of(potentialNextPixel);
                }

            }
            moveType = moveType.getNextMoveType();
        }

        return Optional.empty();
    }

    private static boolean shouldMoveTo(PixelMoveType moveType, Pixel previousPixel, Pixel potentialNextPixel) {
        return isJoinedPixel(previousPixel, potentialNextPixel) &&
                !moveType.isBacktracking(previousPixel.getCameFrom());
    }

    private static boolean isJoinedPixel(Pixel previousPixel, Pixel nextPixel) {
        return previousPixel.getHexColour().equals(nextPixel.getHexColour());
    }

    public static boolean isInBounds(Pixel pixel, BufferedImage image) {
        return pixel.getX() < image.getWidth() && pixel.getX() >= 0 &&
                pixel.getY() < image.getHeight() && pixel.getY() >= 0;
    }

    public static int toRgbInt(String hex) {
        return Color.decode("#" + hex).getRGB();
    }

    public static <T> T getNthLastItemFromList(List<T> list, int n) {
        int listN = n + 1;

        if (list.size() < listN) return null;

        return list.get(list.size() - listN);
    }
}
