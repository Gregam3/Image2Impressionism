package com.greg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColourPatch {
    private Map<Integer, CoordinateBound> yBounds = new HashMap<>();
    private Map<Integer, CoordinateBound> xBounds = new HashMap<>();
    private final List<Pixel> outline;
    //TODO add hex colour
    private List<ColourPatch> childPatches = new ArrayList<>();

    public ColourPatch(List<Pixel> path) {
        for (Pixel borderPixel : path) {
            updateBounds(borderPixel.getX(), borderPixel.getY(), yBounds);
            updateBounds(borderPixel.getY(), borderPixel.getX(), xBounds);
        }
        this.outline = path;
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

        if(xCoordBound == null || yCoordBound == null) {
            return false;
        }

        return yCoordBound.isInBounds(pixel.getY()) && xCoordBound.isInBounds(pixel.getX());
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

    public List<Pixel> generatePatchAreaPixels() {
        List<Pixel> areaPixels = new ArrayList<>();

        for (Map.Entry<Integer, CoordinateBound> boundaryEntry : xBounds.entrySet()) {
            CoordinateBound coordBound = boundaryEntry.getValue();

            for (int y = coordBound.getMin(); y < coordBound.getMax(); y++) {
                areaPixels.add(new Pixel(boundaryEntry.getKey(), y));
            }
        }

        return areaPixels;
    }
}
