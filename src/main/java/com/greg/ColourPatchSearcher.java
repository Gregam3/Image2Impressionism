package com.greg;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ColourPatchSearcher {
    public static List<ColourPatch> search(ColourPatch parentPatch, BufferedImage outputImage, BufferedImage inputImage) {
        List<Pixel> innerPatchPixels = parentPatch.generatePatchAreaPixels(inputImage)
                .stream().filter(areaPixel -> !areaPixel.getHexColour().equals(parentPatch.getHexColour())
                                                && !areaPixel.getHexColour().equals(ColourPatchTracer.BORDER_COLOUR_HEX))
                .collect(Collectors.toList());
        List<ColourPatch> childPatches = new ArrayList<>();

        //For each area pixel
        for (Pixel areaPixel : innerPatchPixels) {
            //Check both that the pixel is not in another patch
            if (!pixelIsInsideExistingPatch(childPatches, areaPixel)) {
                System.out.println("Colour does not match for, x=" + areaPixel.getX() + ", y=" + areaPixel.getY());
                ColourPatch currentPatch = ColourPatchTracer.trace(areaPixel, inputImage, outputImage, parentPatch);
                childPatches.add(currentPatch);
            } else {
                System.out.println("Skipping area pixel x=" + areaPixel.getX() + ", y=" + areaPixel.getY());
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
