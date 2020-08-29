package com.greg;

import spark.Spark;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    public static void main(String[] args) {
        AtomicReference<BufferedImage> outputImage = new AtomicReference<>();

        final File inputFile = new File("src/main/resources/semicircle.png");
        Set<ColourPatch> colourPatches = new HashSet<>();
        Set<String> colours = new HashSet<>();

        //PORT 4567
        Spark.get("/generate", (req, res) -> {
            BufferedImage inputImage;

            try {
                inputImage = ImageIO.read(inputFile);
                outputImage.set(ImageIO.read(inputFile));

                ColourPatch colourPatch = ImageUtil.convert(inputImage);

                searchForInnerColourPatches(colourPatch, outputImage, colourPatches, inputImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            res.header("Content-Type", "image/png");

            System.out.println("Colours:" + colours.size());
            return ImageUtil.getBytesFromImage(outputImage.get());
        });
    }

    private static int searchForInnerColourPatches(ColourPatch colourPatch, AtomicReference<BufferedImage> outputImage, Set<ColourPatch> colourPatches, BufferedImage inputImage) {
        String previousColour = null;
        for (int x = 1; x < bounds.getWidth(); x++) {
            for (int y = 1; y < bounds.getHeight() - 1; y++) {
                Pixel pixel = new Pixel(x, y);

                //TODO find effective way to skip pixels to improve performance
                if (colourPatches.stream()
                        .filter(Objects::nonNull)
                        .filter(patch -> patch.isInside(pixel))
                        .noneMatch(patch -> patch.isInside(pixel))) {
                    pixel.calculateHex(inputImage);
                    System.out.println("Colour does not match for, x=" + x + ", y=" + y);
                    colourPatches.add(ColourPatchTracer.trace(pixel, inputImage, outputImage.get(), bounds));
                    previousColour = pixel.getHex();
                }
            }
        }
        return colourMatchCount;
    }


}



