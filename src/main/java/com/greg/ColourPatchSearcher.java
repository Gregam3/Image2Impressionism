package com.greg;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ColourPatchSearcher {
    public static List<ColourPatch> search(ColourPatch parentPatch, BufferedImage outputImage, BufferedImage inputImage) {
        List<Pixel> areaPixels = parentPatch.generatePatchAreaPixels(inputImage);
        List<ColourPatch> childPatches = new ArrayList<>();

        //For each area pixel
        for (Pixel areaPixel : areaPixels) {
            //Check both that the pixel is not in another patch
            if (parentPatch.getHexColour().equals(areaPixel.getHexColour()) && !isInsideAnotherColorPatch(childPatches, areaPixel)) {
                System.out.println("Colour does not match for, x=" + areaPixel.getX() + ", y=" + areaPixel.getY());
                ColourPatch currentPatch = ColourPatchTracer.trace(areaPixel, inputImage, outputImage);

                childPatches.add(currentPatch);
            }
        }

        return childPatches;
    }

    public static boolean isInsideAnotherColorPatch(List<ColourPatch> patches, Pixel pixel) {
        return patches.stream()
                .filter(Objects::nonNull)
                .noneMatch(patch -> patch.isInside(pixel));
    }
}
