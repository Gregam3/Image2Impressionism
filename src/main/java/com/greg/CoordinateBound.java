package com.greg;

public class CoordinateBound {
    private int max;
    private int min;

    public CoordinateBound(int max, int min) {
        this.max = max;
        this.min = min;
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

    public boolean isInBounds(int value) {
        return value >= min && value <= max;
    }
}
