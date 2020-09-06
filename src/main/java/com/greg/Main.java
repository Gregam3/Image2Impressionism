package com.greg;

import spark.Spark;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    static int pixelsScanned = 0;

    public static void main(String[] args) {
        try {
            AtomicReference<byte[]> outputBytes = new AtomicReference<>();
            final File inputFile = new File("src/main/resources/shitty_finnish_flag.png");
            BufferedImage outputImage = ImageIO.read(inputFile);

            //PORT 4567
            Spark.get("/generate", (req, res) -> {
                outputBytes.set(ImageUtil.getBytesFromImage(outputImage));

                res.header("Content-Type", "image/png");
                return outputBytes.get();
            });

            BufferedImage inputImage = cloneImage(outputImage);

            ColourPatch colourPatch = ImageUtil.convert(inputImage, outputImage);

            colourPatch.setChildPatches(ColourPatchSearcher.search(colourPatch, outputImage, inputImage));
            List<ColourPatch> childPatches = colourPatch.getChildPatches();

//            while(!childPatches.isEmpty()) {
//                for (ColourPatch childPatch : childPatches) {
//                    childPatch.setChildPatches(ColourPatchSearcher.search(childPatch, outputImage, inputImage));
//                }
//
//                childPatches = childPatches.stream()
//                        .flatMap(patch -> patch.getChildPatches().stream())
//                        .collect(Collectors.toList());
//            }

            System.out.println("Done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static BufferedImage cloneImage(BufferedImage image) {
        BufferedImage imageClone = new BufferedImage(Math.round(image.getWidth()), Math.round(image.getHeight()), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = imageClone.createGraphics();
        graphics.drawImage(image, 0, 0, null);

        return imageClone;
    }
}




