package com.greg;

import java.awt.image.BufferedImage;

public class Pixel {
    private final int x;
    private final int y;
    private Integer rgb;
    private PixelMoveType cameFrom;

    public Pixel(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Pixel(int x, int y, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.calculateHex(image);
    }

    public static boolean haveSameLocation(Pixel thisPixel, Pixel thatPixel) {
        if (thisPixel == null || thatPixel == null) return false;

        return thisPixel.getX() == thatPixel.getX() && thisPixel.getY() == thatPixel.getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Integer getRgb() {
        return rgb;
    }

    public PixelMoveType getCameFrom() {
        return cameFrom;
    }

    public void setCameFrom(PixelMoveType moveType) {
        this.cameFrom = moveType;
    }

    @Override
    public String toString() {
        return "x=" + x +
                " y=" + y +
                " hexColour='" + rgb + '\'';
    }

    public void calculateHex(BufferedImage image) {
        if(ColourPatchTracer.isInBounds(this, image)) {
            this.rgb = image.getRGB(x, y);
        } else {
            throw new AssertionError("Pixel out of bounds " + this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pixel pixel = (Pixel) o;

        if (x != pixel.x) return false;
        return y == pixel.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
