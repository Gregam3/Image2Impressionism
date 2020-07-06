package com.greg;

import spark.Spark;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private static BufferedImage inputImage;
    private static BufferedImage outputImage;
    private static Rectangle bounds;

    public static void main(String[] args) {
        final File inputFile = new File("src/main/resources/ukraine_flag.png");

        Spark.get("/generate", (req, res) -> {
            try {
                inputImage = ImageIO.read(inputFile);
                bounds = Main.inputImage.getData().getBounds();

                String previousColour = null;

                for (int x = 1; x < bounds.getWidth(); x++) {
                    for (int y = 1; y < bounds.getWidth(); y++) {
                        Pixel pixel = new Pixel(x, y);
                        pixel.setHex(inputImage);

                        boolean colorMatches = previousColour != null && previousColour.equals(pixel.getHex());

                        if (!colorMatches) {
                            System.out.println("Colour matches");
                            int rgbInt = new Random().nextInt(16777215);
                            colorBorder(pixel, new ArrayList<>(), rgbInt, PixelMoveType.UP);
                        }

                        previousColour = pixel.getHex();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            res.header("Content-Type", "image/png");

            return getBytesFromImage();
        });
    }

    private static byte[] getBytesFromImage() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(inputImage, "png", baos);
        return baos.toByteArray();
    }

    private static void colorBorder(Pixel pixel, List<Pixel> path, int color, PixelMoveType moveType) {
        if (pixel.getHex() != null) {
            pixel.setHex(inputImage);
        }

//        System.out.println(pixel.toString());
        inputImage.setRGB(pixel.getX(), pixel.getY(), color);

        if (!path.isEmpty() && path.get(0) == pixel) {
            return;
        }

        path.add(pixel);

        Pixel potentialNextPixel;

        for (int i = 0; i < PixelMoveType.values().length; i++) {
            potentialNextPixel = moveType.movePixel(pixel);
            moveType = moveType.getNextMoveType();

            if(isJoinedPixel(pixel, potentialNextPixel, path)) {
                colorBorder(potentialNextPixel, path, color, moveType);
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
        return num >= 0 && num < max;
    }
}
