package com.greg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColourPatch {
    private Map<Integer, CoordinateBound> yBounds = new HashMap<>();
    private Map<Integer, CoordinateBound> xBounds = new HashMap<>();

    //TODO add child colour patches

    public ColourPatch(List<Pixel> path) {
        for (Pixel borderPixel : path) {
            updateBounds(borderPixel.getX(), borderPixel.getY(), yBounds);
            updateBounds(borderPixel.getY(), borderPixel.getX(), xBounds);
        }
    }

    private void updateBounds(int keyCoordinate, int valueCoordinate, Map<Integer, CoordinateBound> bounds) {
        CoordinateBound bound = bounds.get(keyCoordinate);

        if (bound == null) {
            yBounds.put(keyCoordinate, new CoordinateBound(valueCoordinate, valueCoordinate));
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
        return yBounds.get(pixel.getX()).isInBounds(pixel.getY()) &&
                xBounds.get(pixel.getY()).isInBounds(pixel.getY());
    }
}
