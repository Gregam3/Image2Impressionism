package com.greg;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
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

    private final BufferedImage UKRAINE_FLAG_IMAGE = getImageFromFile("src/main/resources/ukraine_flag.png");

    private BufferedImage getImageFromFile(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void testColourPatchTracing() {
        ColourPatch parentPatch = ImageUtil.convert(UKRAINE_FLAG_IMAGE, UKRAINE_FLAG_IMAGE);

        ColourPatch colourPatch = ColourPatchTracer.trace(
                new Pixel(1, 1, UKRAINE_FLAG_IMAGE),
                UKRAINE_FLAG_IMAGE,
                UKRAINE_FLAG_IMAGE,
                parentPatch);

        assert colourPatch.isInside(new Pixel(899, 299));
        assert colourPatch.isInside(new Pixel(1, 1));
        assert !colourPatch.isInside(new Pixel(900, 300));
        assert !colourPatch.isInside(new Pixel(0, 0));

    }

    @Test
    public void testGenerateAreaPixels() {
        ColourPatch colourPatch = ImageUtil.convert(UKRAINE_FLAG_IMAGE, UKRAINE_FLAG_IMAGE);

        List<Pixel> areaPixels = colourPatch.generatePatchAreaPixels(UKRAINE_FLAG_IMAGE);

        Rectangle bounds = UKRAINE_FLAG_IMAGE.getData().getBounds();
        Assertions.assertEquals(bounds.getWidth() * bounds.getHeight(), areaPixels.size());
    }

    private final BufferedImage PALAU_FLAG_IMAGE = getImageFromFile("src/main/resources/palau_flag.png");

    @Test
    public void testOutlineCircle() {
        ColourPatch colourPatch = ImageUtil.convert(PALAU_FLAG_IMAGE, PALAU_FLAG_IMAGE);

        ColourPatchSearcher.search(colourPatch, PALAU_FLAG_IMAGE, PALAU_FLAG_IMAGE);
    }
}