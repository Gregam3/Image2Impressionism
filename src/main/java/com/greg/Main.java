package com.greg;

import spark.Spark;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

//PORT 4567
public class Main {
    public static void main(String[] args) {
        AtomicReference<BufferedImage> outputImage = new AtomicReference<>();

        final File inputFile = new File("src/main/resources/test2.png");
        Set<ColourPatch> colourPatches = new HashSet<>();
        Set<String> colours = new HashSet<>();

        Spark.get("/generate", (req, res) -> {
            BufferedImage inputImage;
            Rectangle bounds;

            int colourMatchCount = 0;
            try {
                inputImage = ImageIO.read(inputFile);
                bounds = inputImage.getData().getBounds();

                outputImage.set(ImageIO.read(inputFile));

                String previousColour = null;

                for (int x = 1; x < bounds.getWidth(); x++) {
                    for (int y = 1; y < bounds.getHeight() - 1; y++) {
                        Pixel pixel = new Pixel(x, y);
                        pixel.calculateHex(inputImage);

                        if (colourPatches.stream().filter(Objects::nonNull)
                                .noneMatch(patch -> patch.isInside(pixel))) {
                            colourMatchCount++;
                            System.out.println("Colour does not match for, x=" + x + ", y=" + y);
                            colourPatches.add(ColourPatchTracer.trace(pixel, inputImage, outputImage.get(), bounds));
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
            return getBytesFromImage(outputImage.get());
        });
    }

    private static byte[] getBytesFromImage(BufferedImage outputImage) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(outputImage, "png", baos);
        return baos.toByteArray();
    }
}
