package com.greg;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

class ColourPatchTest {
    @Test
    public void testUpdateBounds() {
        List<Pixel> path = Arrays.asList(
                new Pixel(1, 1),
                new Pixel(2, 1),
                new Pixel(3, 1),
                new Pixel(3, 2),
                new Pixel(2, 2),
                new Pixel(1, 2)
        );


        ColourPatch colourPatch = new ColourPatch(path);

        assert path.stream().allMatch(colourPatch::isInside);

        List<Pixel> outsidePath = Arrays.asList(
                new Pixel(0, 0),
                new Pixel(0, 1),
                new Pixel(1, 0),
                new Pixel(4, 4),
                new Pixel(4, 1),
                new Pixel(1, 4)
        );

        assert outsidePath.stream().noneMatch(colourPatch::isInside);
    }

    @Test
    public void testColourPatchTracing() {
        final File inputFile = new File("src/main/resources/test2.png");

        BufferedImage inputImage = null;
        try {
            inputImage = ImageIO.read(inputFile);

            ColourPatch colourPatch = ColourPatchTracer.trace(
                    new Pixel(1, 1, inputImage),
                    inputImage,
                    ImageIO.read(inputFile),
                    inputImage.getData().getBounds()
            );

            System.out.println();
        } catch (IOException e) {
            Assertions.fail(e);
        }
    }
}