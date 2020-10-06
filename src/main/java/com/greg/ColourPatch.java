package com.greg;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColourPatch {
    //                x        y bounds
    private final Map<Integer, CoordinateBound> yBoundsForX = new HashMap<>();
    //                y        x bounds
    private final Map<Integer, CoordinateBound> xBoundsForY = new HashMap<>();
    private final List<Pixel> outline;
    private String hex;
    private List<ColourPatch> childPatches = new ArrayList<>();

    public ColourPatch(List<Pixel> path) {
        for (Pixel borderPixel : path) {
            updateBounds(borderPixel.getY(), borderPixel.getX(), xBoundsForY);
            updateBounds(borderPixel.getX(), borderPixel.getY(), yBoundsForX);
        }
        this.outline = path;

        if (!path.isEmpty()) {
            this.hex = path.get(0).getHex();
        }
    }

    public void updateBounds(int keyCoordinate, int valueCoordinate, Map<Integer, CoordinateBound> bounds) {
        final CoordinateBound bound = bounds.get(keyCoordinate);

        if (bound == null) {
            bounds.put(keyCoordinate, new CoordinateBound(valueCoordinate));
        } else {
            if (valueCoordinate > bound.getMax()) {
                bound.setMax(valueCoordinate);
            }

            if (valueCoordinate < bound.getMin()) {
                bound.setMin(valueCoordinate);
            }
        }
    }

    public boolean isInside(Pixel pixel) {
        CoordinateBound yCoordBound = yBoundsForX.get(pixel.getX());
        CoordinateBound xCoordBound = xBoundsForY.get(pixel.getY());

        if (xCoordBound == null || yCoordBound == null) {
            return false;
        }

        return xCoordBound.isInBounds(pixel.getX()) && yCoordBound.isInBounds(pixel.getY());
    }

    public List<Pixel> generatePatchAreaPixels(BufferedImage image) {
        List<Pixel> areaPixels = new ArrayList<>();

        for (Map.Entry<Integer, CoordinateBound> boundaryEntry : yBoundsForX.entrySet()) {
            CoordinateBound coordBound = boundaryEntry.getValue();

            for (int y = coordBound.getMin() + 1; y <= coordBound.getMax() - 1; y++) {
                Pixel pixel = new Pixel(boundaryEntry.getKey(), y, image);
                System.out.println(pixel);
                areaPixels.add(pixel);
            }
        }

        return areaPixels;
    }

    public List<Pixel> getOutline() {
        return outline;
    }

    public List<ColourPatch> getChildPatches() {
        return childPatches;
    }

    public void setChildPatches(List<ColourPatch> childPatches) {
        this.childPatches = childPatches;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }
}
