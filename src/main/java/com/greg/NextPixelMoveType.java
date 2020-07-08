package com.greg;

public class NextPixelMoveType {
    private final PixelMoveType nextMoveOutwards;
    private final PixelMoveType nextMoveInwards;

    public NextPixelMoveType(PixelMoveType nextMoveOutwards, PixelMoveType nextMoveInwards) {
        this.nextMoveOutwards = nextMoveOutwards;
        this.nextMoveInwards = nextMoveInwards;
    }

    public PixelMoveType getNextMoveOutwards() {
        return nextMoveOutwards;
    }

    public PixelMoveType getNextMoveInwards() {
        return nextMoveInwards;
    }
}
