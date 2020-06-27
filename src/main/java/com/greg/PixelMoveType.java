package com.greg;

import java.awt.image.BufferedImage;

public enum PixelMoveType {
    RIGHT(pixel -> new Pixel(pixel.getX() + 1, pixel.getY())),
    DOWN(pixel -> new Pixel(pixel.getX(), pixel.getY() - 1)),
    LEFT(pixel -> new Pixel(pixel.getX() - 1, pixel.getY() + 1)),
    UP(pixel -> new Pixel(pixel.getX(), pixel.getY() + 1));

    private final PixelMove move;

    PixelMoveType(PixelMove move) {
        this.move = move;
    }

    public Pixel movePixel(Pixel pixel) {
        return move.movePixel(pixel);
    }
}
