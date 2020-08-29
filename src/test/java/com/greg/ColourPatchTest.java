package com.greg;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
                new Pixel(0,0),
                new Pixel(0,1),
                new Pixel(1,0),
                new Pixel(4,4),
                new Pixel(4,1),
                new Pixel(1,4)
        );

        assert outsidePath.stream().noneMatch(colourPatch::isInside);
    }
}