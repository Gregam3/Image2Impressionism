package com.greg;

import java.awt.image.BufferedImage;

public class Pixel {
    private final int x;
    private final int y;
    private String hex;

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
        return thisPixel.getX() == thatPixel.getX() && thisPixel.getY() == thatPixel.getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getHex() {
        return hex;
    }

    @Override
    public String toString() {
        return "x=" + x +
                " y=" + y +
                " hex='" + hex + '\'';
    }

    public void calculateHex(BufferedImage image) {
        this.hex = Integer.toHexString(image.getRGB(x, y));
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
