package com.greg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColourPatch {
    private Map<Integer, CoordinateBound> yBounds = new HashMap<>();
    private Map<Integer, CoordinateBound> xBounds = new HashMap<>();
    //TODO add hex colour
    //TODO add child colour patches

    public ColourPatch(List<Pixel> path) {
        for (Pixel borderPixel : path) {
            updateBounds(borderPixel.getX(), borderPixel.getY(), yBounds);
            updateBounds(borderPixel.getY(), borderPixel.getX(), xBounds);
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

        if(xCoordBound == null || yCoordBound == null) {
            return false;
        }

        return yCoordBound.isInBounds(pixel.getY()) &&
                xCoordBound.isInBounds(pixel.getY());
    }
}
