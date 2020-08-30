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
            if (!areaPixel.getHexColour().equals(parentPatch.getHexColour()) && !pixelIsInsideExistingPatch(childPatches, areaPixel)) {
                System.out.println("Colour does not match for, x=" + areaPixel.getX() + ", y=" + areaPixel.getY());
                ColourPatch currentPatch = ColourPatchTracer.trace(areaPixel, inputImage, outputImage);

                childPatches.add(currentPatch);
            }
        }

        return childPatches;
    }

    public static boolean pixelIsInsideExistingPatch(List<ColourPatch> existingPatches, Pixel pixel) {
        return existingPatches.stream()
                .filter(Objects::nonNull)
                .anyMatch(patch -> patch.isInside(pixel));
    }
}
