package com.greg;

import spark.Spark;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    public static void main(String[] args) {
        AtomicReference<BufferedImage> outputImage = new AtomicReference<>();

        final File inputFile = new File("src/main/resources/semicircle.png");

        //PORT 4567
        Spark.get("/generate", (req, res) -> {
            BufferedImage inputImage;

            try {
                inputImage = ImageIO.read(inputFile);
                outputImage.set(ImageIO.read(inputFile));

                ColourPatch colourPatch = ImageUtil.convert(inputImage);

                colourPatch.setChildPatches(ColourPatchSearcher.search(colourPatch, outputImage.get(), inputImage));
            } catch (IOException e) {
                e.printStackTrace();
            }

            res.header("Content-Type", "image/png");
            return ImageUtil.getBytesFromImage(outputImage.get());
        });
    }
}




