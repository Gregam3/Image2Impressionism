package com.greg;

import spark.Spark;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    public static void main(String[] args) {
        AtomicReference<byte[]> outputBytes = new AtomicReference<>();
        final File inputFile = new File("src/main/resources/palau_flag.png");

        //PORT 4567
        Spark.get("/generate", (req, res) -> {
            BufferedImage inputImage;

            try {
                inputImage = ImageIO.read(inputFile);
                BufferedImage outputImage = (ImageIO.read(inputFile));

                ColourPatch colourPatch = ImageUtil.convert(inputImage, outputImage);

                List<ColourPatch> childPatches;

                do {
                    childPatches = ColourPatchSearcher.search(colourPatch, outputImage, inputImage);

                    colourPatch.setChildPatches(childPatches);
                } while(!childPatches.isEmpty());

                outputBytes.set(ImageUtil.getBytesFromImage(outputImage));
            } catch (IOException e) {
                e.printStackTrace();
            }

            res.header("Content-Type", "image/png");
            return outputBytes.get();
        });
    }
}




