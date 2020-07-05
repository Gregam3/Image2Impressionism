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
    private static BufferedImage image;
    private static Rectangle bounds;

    public static void main(String[] args) {
        final File imageFile = new File("src/main/resources/monkey.png");
        Spark.get("/hello", (req, res) -> {

            try {
                image = ImageIO.read(imageFile);
                bounds = image.getData().getBounds();

                String previousColour = null;

                for (int x = 0; x < bounds.getWidth(); x++) {
                    for (int y = 0; y < bounds.getWidth(); y++) {
                        Pixel pixel = new Pixel(x, y);
                        pixel.setHex(image);

                        boolean colorMatches = previousColour != null && previousColour.equals(pixel.getHex());

                        if(!colorMatches) {
                            int rgbInt = new Random().nextInt(0);
                            colorBorder(pixel, new ArrayList<>(), rgbInt);
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
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

    private static void colorBorder(Pixel pixel, List<Pixel> path, int color) {
        if(pixel.getHex() != null) {
            pixel.setHex(image);
        }

        System.out.println(pixel.toString());
        image.setRGB(pixel.getX(), pixel.getY(), 16711680);

        if (!path.isEmpty() && path.get(0) == pixel) {
            return;
        }

        path.add(pixel);

        for (PixelMoveType pixelMoveType : PixelMoveType.values()) {
            Pixel potentialNextPixel = pixelMoveType.movePixel(pixel);
            if (isJoinedPixel(pixel, potentialNextPixel, path)) {
                colorBorder(potentialNextPixel, path, color);
                return;
            }
        }
    }

    private static boolean isJoinedPixel(Pixel lastPixel, Pixel nextPixel, List<Pixel> path) {
        boolean isInBounds = isInBounds(nextPixel.getX(), bounds.getWidth()) && isInBounds(nextPixel.getY(), bounds.getHeight());

        if (isInBounds && !path.contains(nextPixel)) {
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
