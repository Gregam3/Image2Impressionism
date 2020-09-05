package com.greg;

public class CoordinateBound {
    private int max;
    private int min;

    public CoordinateBound(int value) {
        this.min = value;
        this.max = value;
    }

    public CoordinateBound(int max, int min) {
        this.min = min;
        this.max = max;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @param value opposite value, to find x bound pass y
     * @return
     */
    public boolean isInBounds(int value) {
        return value >= min && value <= max;
    }
}
