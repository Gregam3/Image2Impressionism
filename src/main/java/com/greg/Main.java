package com.greg;

import spark.Spark;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
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

    private static List<ColourPatch> searchForInnerColourPatches(ColourPatch colourPatch, BufferedImage outputImage, Set<ColourPatch> colourPatches, BufferedImage inputImage) {
        //First inner pixel
        Pixel currentPixel = colourPatch.generatePatchAreaPixels();

        //TODO find effective way to skip pixels to improve performance
        if (colourPatches.stream()
                .filter(Objects::nonNull)
                .filter(patch -> patch.isInside(currentPixel))
                .noneMatch(patch -> patch.isInside(currentPixel))) {
            currentPixel.calculateHex(inputImage);
            System.out.println("Colour does not match for, x=" + currentPixel.getX() + ", y=" + y);
            colourPatches.add(ColourPatchTracer.trace(currentPixel, inputImage, outputImage));
        }
    }
}




