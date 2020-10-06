package com.greg;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ColourPatchTracer {
    static final int BORDER_COLOUR_RGB = new Color(255, 0, 0).getRGB();
    static final String BORDER_COLOUR_HEX = Integer.toHexString(BORDER_COLOUR_RGB);

    public static ColourPatch trace(Pixel pixel, BufferedImage inputImage, BufferedImage outputImage) {
        return trace(pixel, new ArrayList<>(), inputImage, outputImage);
    }

    /**
     * Finds the outline of a colour patch and traces the outline in BORDER_COLOUR
     *
     * @param pixel       The first pixel
     * @param inputImage  The original image
     * @param outputImage The image to add an outline to
     */
    public static ColourPatch trace(Pixel pixel, List<Pixel> path, BufferedImage inputImage, BufferedImage outputImage) {
        PixelMoveType moveType = PixelMoveType.RIGHT;

        while (path.size() <= 1 || !Pixel.haveSameLocation(path.get(0), pixel)) {
            if (pixel.getHex() != null) {
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

            if(pixel.getX() == 517 && pixel.getY() == 189) {
                System.out.println();
            }

            Optional<Pixel> nextPixel = getNextPixel(moveType, path, inputImage);

            if (nextPixel.isPresent()) {
                pixel = nextPixel.get();
                moveType = pixel.getCameFrom();
            } else {
                pixel.setDeadEnd(true);
                Pixel previousPixel = getNthLastPixel(path, path.size() - 2);
                ColourPatch regressionPath = trace(previousPixel, path, inputImage, outputImage);
                path.addAll(regressionPath.getOutline());
                return new ColourPatch(path);
            }
        }

        return new ColourPatch(path);
    }

    private static Optional<Pixel> getNextPixel(PixelMoveType moveType, List<Pixel> path, BufferedImage image) {
        moveType = moveType.getFirstAttemptMove();
        Pixel currentPixel = getNthLastPixel(path, path.size() - 1);

        for (int moveIndex = 0; moveIndex < PixelMoveType.values().length; moveIndex++) {
            Pixel potentialNextPixel = moveType.movePixel(currentPixel);
            if (isInBounds(potentialNextPixel, image)) {
                potentialNextPixel.calculateHex(image);

                if (shouldMoveTo(moveType, currentPixel, potentialNextPixel, path)) {
                    potentialNextPixel.setCameFrom(moveType);
                    return Optional.of(potentialNextPixel);
                }

            }
            moveType = moveType.getNextMoveType();
        }

        return Optional.empty();
    }

    private static Pixel getPreviousPixel(List<Pixel> path) {
        if(path.size() < 2) {
            return null;
        }

        return path.get(path.size() - 2);
    }

    private static boolean shouldMoveTo(PixelMoveType moveType, Pixel currentPixel, Pixel potentialNextPixel, List<Pixel> path) {
        return isJoinedPixel(currentPixel, potentialNextPixel) &&
                (!path.contains(potentialNextPixel) || path.get(0).equals(potentialNextPixel)) &&
                !moveType.isBacktracking(currentPixel.getCameFrom());
    }

    private static boolean isJoinedPixel(Pixel previousPixel, Pixel nextPixel) {
        return previousPixel.getHex().equals(nextPixel.getHex());
    }

    public static boolean isInBounds(Pixel pixel, BufferedImage image) {
        return pixel.getX() < image.getWidth() && pixel.getX() >= 0 &&
                pixel.getY() < image.getHeight() && pixel.getY() >= 0;
    }

    public static int toRgbInt(String hex) {
        return Color.decode("#" + hex).getRGB();
    }

    public static Pixel getNthLastPixel(List<Pixel> path, int n) {
        if (path.size() < n) return null;

        Pixel pixel;

        for (int i = n; i >= 0; --i) {
            pixel = path.get(i);

            if(!pixel.isDeadEnd()) {
                return pixel;
            }
        }

        throw new AssertionError("UH OH STINKY");

//        return null;
    }
}
