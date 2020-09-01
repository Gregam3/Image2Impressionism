package com.greg;

import spark.Spark;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    static int pixelsScanned = 0;

    public static void main(String[] args) {
        try {
            AtomicReference<byte[]> outputBytes = new AtomicReference<>();
            final File inputFile = new File("src/main/resources/palau_flag.png");
            BufferedImage outputImage = ImageIO.read(inputFile);

            //PORT 4567
            Spark.get("/generate", (req, res) -> {
                outputBytes.set(ImageUtil.getBytesFromImage(outputImage));

                res.header("Content-Type", "image/png");
                return outputBytes.get();
            });

            BufferedImage inputImage = ImageIO.read(inputFile);

            ColourPatch colourPatch = ImageUtil.convert(inputImage, outputImage);

            List<ColourPatch> childPatches;

            do {
                childPatches = ColourPatchSearcher.search(colourPatch, outputImage, inputImage);

                colourPatch.setChildPatches(childPatches);
            } while (!childPatches.isEmpty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}




