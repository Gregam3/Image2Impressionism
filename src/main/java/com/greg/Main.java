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

//PORT 4567
public class Main {
    public static final String BORDER_COLOUR = "FF0000";
    private static BufferedImage inputImage;
    private static BufferedImage outputImage;
    private static Rectangle bounds;

    public static void main(String[] args) {
        final File inputFile = new File("src/main/resources/test2.png");
        Set<ColourPatch> colourPatches = new HashSet<>();
        Set<String> colours = new HashSet<>();

        Spark.get("/generate", (req, res) -> {
            int colourMatchCount = 0;
            try {
                inputImage = ImageIO.read(inputFile);
                bounds = inputImage.getData().getBounds();

                outputImage = ImageIO.read(inputFile);

                String previousColour = null;

                for (int x = 1; x < bounds.getWidth(); x++) {
                    for (int y = 1; y < bounds.getHeight() - 1; y++) {
                        Pixel pixel = new Pixel(x, y);
                        pixel.setHex(inputImage);

                        System.out.println("Checking if " + pixel.toString() + " is a new colour ");

                        if (colourPatches.stream().filter(Objects::nonNull)
                                .noneMatch(patch -> patch.isInside(pixel))) {
                            colourMatchCount++;
                            System.out.println("Colour does not match for, x=" + x + ", y=" + y);
//                            int rgbInt = new Random().nextInt(16777215);
                            colourPatches.add(getColourPatch(pixel));
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

    private static byte[] getBytesFromImage() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(outputImage, "png", baos);
        return baos.toByteArray();
    }

    private static ColourPatch getColourPatch(Pixel pixel) {
        List<Pixel> path = new ArrayList<>();
        PixelMoveType moveType = PixelMoveType.RIGHT;

        while (path.isEmpty() || (path.get(0).getX() != pixel.getX() && path.get(0).getY() == pixel.getY())) {
            if (pixel.getHex() != null) {
                pixel.setHex(inputImage);
            }

            System.out.println(pixel.toString());
            outputImage.setRGB(pixel.getX(), pixel.getY(), toRgbInt(BORDER_COLOUR));

            path.add(pixel);

            moveType = moveType.getFirstAttemptMove();

            for (int i = 0; i < PixelMoveType.values().length; i++) {
                Pixel potentialNextPixel = moveType.movePixel(pixel);

                if (isJoinedPixel(pixel, potentialNextPixel)) {
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

    private static boolean isJoinedPixel(Pixel lastPixel, Pixel nextPixel) {
        boolean isInBounds = isInBounds(nextPixel.getX(), bounds.getWidth()) && isInBounds(nextPixel.getY(), bounds.getHeight());

        if (isInBounds) {
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
