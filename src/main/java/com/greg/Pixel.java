package com.greg;

import java.awt.image.BufferedImage;

public class Pixel {
    private final int x;
    private final int y;
    private String hex;

    public Pixel(int x, int y, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.hex = Integer.toHexString(image.getRGB(x, y));
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
        return "PixelCoordinate{" +
                "x=" + x +
                ", y=" + y +
                ", hex='" + hex + '\'' +
                '}';
    }
}
