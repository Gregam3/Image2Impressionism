package com.greg;

import spark.Spark;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Main {
    private static BufferedImage inputImage;
    private static BufferedImage outputImage;
    private static Rectangle bounds;

    public static void main(String[] args) {
        final File inputFile = new File("src/main/resources/test2.png");

        Spark.get("/generate", (req, res) -> {
            int colourMatchCount = 0;
            Set<String> colours = new HashSet<>();
            try {
                inputImage = ImageIO.read(inputFile);
                bounds = inputImage.getData().getBounds();

                outputImage = ImageIO.read(inputFile);

                String previousColour = null;

                for (int x = 1; x < bounds.getWidth(); x++) {
                    for (int y = 1; y < bounds.getHeight() - 1; y++) {
                        Pixel pixel = new Pixel(x, y);
                        pixel.setHex(inputImage);

                        boolean colorMatches = previousColour != null && previousColour.equals(pixel.getHex());

                        if (!colorMatches) {
                            colourMatchCount++;
                            System.out.println("Colour matches");
//                            int rgbInt = new Random().nextInt(16777215);
                            colorBorder(pixel, new ArrayList<>(), "FF0000", PixelMoveType.UP);
                        }

                        previousColour = pixel.getHex();
                        colours.add(previousColour);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            res.header("Content-Type", "image/png");

            System.out.println("Colours Matches:" + colourMatchCount);
            System.out.println("Colours:" + colours.size());
            return getBytesFromImage();
        });
    }

    private static BufferedImage clearImage(BufferedImage input) {
        BufferedImage resultImage = input;

        for (int x = 1; x < bounds.getWidth(); x++) {
            for (int y = 1; y < bounds.getHeight() - 1; y++) {
                resultImage.setRGB(x, y, 8224125);
            }
        }
        return resultImage;
    }

    private static byte[] getBytesFromImage() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(outputImage, "png", baos);
        return baos.toByteArray();
    }

    private static void colorBorder(Pixel pixel, List<Pixel> path, String colorHex, PixelMoveType moveType) {
        if (pixel.getHex() != null) {
            pixel.setHex(inputImage);
        }

        System.out.println(pixel.toString());
        outputImage.setRGB(pixel.getX(), pixel.getY(), toRgbInt(colorHex));

        if (!path.isEmpty() && path.get(0) == pixel) {
            return;
        }

        path.add(pixel);

        Pixel potentialNextPixel;

        for (int i = 0; i < PixelMoveType.values().length; i++) {
            potentialNextPixel = moveType.movePixel(pixel);
            moveType = moveType.getNextMoveType();

            if (isJoinedPixel(pixel, potentialNextPixel, path)) {
                colorBorder(potentialNextPixel, path, colorHex, moveType);
                return;
            }
        }

        System.out.println(path.size());
    }

    private static boolean isJoinedPixel(Pixel lastPixel, Pixel nextPixel, List<Pixel> path) {
        boolean isInBounds = isInBounds(nextPixel.getX(), bounds.getWidth()) && isInBounds(nextPixel.getY(), bounds.getHeight());

        if (isInBounds && !path.contains(nextPixel)) {
            nextPixel.setHex(inputImage);
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
