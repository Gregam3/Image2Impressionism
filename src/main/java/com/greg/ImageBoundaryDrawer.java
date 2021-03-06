package com.greg;

import spark.Spark;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class ImageBoundaryDrawer {
    private static final AtomicReference<byte[]> outputBytes = new AtomicReference<>();
    private static final File inputFile = new File("src/main/resources/shitty_scenary.png");
    private static BufferedImage outputImage;

    static int pixelsScanned = 0;

    public static void main(String[] args) {
        //PORT 4567
        Spark.get("/generate", (req, res) -> {
            outputBytes.set(ImageUtil.getBytesFromImage(outputImage));

            res.header("Content-Type", "image/png");
            return outputBytes.get();
        });

        drawImageBoundaries(inputFile);
    }

    public static Optional<BufferedImage> drawImageBoundaries(File inputFile) {
        try {
            outputImage = ImageIO.read(inputFile);
            BufferedImage inputImage = cloneImage(outputImage);

            ColourPatch colourPatch = ImageUtil.convert(inputImage, outputImage);

            colourPatch.setChildPatches(ColourPatchSearcher.search(colourPatch, outputImage, inputImage));
            List<ColourPatch> childPatches = colourPatch.getChildPatches();

            while(!childPatches.isEmpty()) {
                for (ColourPatch childPatch : childPatches) {
                    //Gets stuck on child patches 11
                    if(!childPatch.getHex().equals(ColourPatchTracer.BORDER_COLOUR_HEX)) {
                        childPatch.setChildPatches(ColourPatchSearcher.search(childPatch, outputImage, inputImage));
                    }
                }

                childPatches = childPatches.stream()
                        .flatMap(patch -> patch.getChildPatches().stream())
                        .collect(Collectors.toList());
            }

            System.out.println("Done");
            return Optional.of(outputImage);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private static BufferedImage cloneImage(BufferedImage image) {
        BufferedImage imageClone = new BufferedImage(Math.round(image.getWidth()), Math.round(image.getHeight()), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = imageClone.createGraphics();
        graphics.drawImage(image, 0, 0, null);

        return imageClone;
    }

    public static BufferedImage getImageFromFile(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}




