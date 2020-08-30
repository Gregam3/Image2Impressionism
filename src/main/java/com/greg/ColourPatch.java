package com.greg;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColourPatch {
    private Map<Integer, CoordinateBound> yBounds = new HashMap<>();
    private Map<Integer, CoordinateBound> xBounds = new HashMap<>();
    private final List<Pixel> outline;
    private String hexColour;
    private List<ColourPatch> childPatches = new ArrayList<>();

    public ColourPatch(List<Pixel> path, boolean calculateHex) {
        for (Pixel borderPixel : path) {
            updateBounds(borderPixel.getX(), borderPixel.getY(), yBounds);
            updateBounds(borderPixel.getY(), borderPixel.getX(), xBounds);
        }
        this.outline = path;

        if (calculateHex) {
            this.hexColour = path.get(0).getHexColour();
        }
    }

    public void updateBounds(int keyCoordinate, int valueCoordinate, Map<Integer, CoordinateBound> bounds) {
        final CoordinateBound bound = bounds.get(keyCoordinate);

        if (bound == null) {
            bounds.put(keyCoordinate, new CoordinateBound(valueCoordinate));
        } else {
            if (bound.getMax() < valueCoordinate) {
                bound.setMax(valueCoordinate);
            }

            if (bound.getMin() > valueCoordinate) {
                bound.setMin(valueCoordinate);
            }
        }
    }

    public boolean isInside(Pixel pixel) {
        CoordinateBound yCoordBound = yBounds.get(pixel.getX());
        CoordinateBound xCoordBound = xBounds.get(pixel.getY());

        if (xCoordBound == null || yCoordBound == null) {
            return false;
        }

        return yCoordBound.isInBounds(pixel.getY()) && xCoordBound.isInBounds(pixel.getX());
    }

    public List<Pixel> generatePatchAreaPixels(BufferedImage image) {
        List<Pixel> areaPixels = new ArrayList<>();

        for (Map.Entry<Integer, CoordinateBound> boundaryEntry : yBounds.entrySet()) {
            CoordinateBound coordBound = boundaryEntry.getValue();

            for (int y = coordBound.getMin(); y <= coordBound.getMax(); y++) {
                Pixel pixel = new Pixel(boundaryEntry.getKey(), y);
                System.out.println(pixel);
                pixel.calculateHex(image);
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

    public String getHexColour() {
        return hexColour;
    }

    public void setHexColour(String hexColour) {
        this.hexColour = hexColour;
    }
}
