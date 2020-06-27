package com.greg;

import java.awt.image.BufferedImage;

public enum PixelMoveType {
    RIGHT((image, pixel) -> new Pixel(pixel.getX() + 1, pixel.getY(), image)),
    DOWN((image, pixel) -> new Pixel(pixel.getX(), pixel.getY() - 1, image)),
    LEFT((image, pixel) -> new Pixel(pixel.getX() - 1, pixel.getY() + 1, image)),
    UP((image, pixel) -> new Pixel(pixel.getX(), pixel.getY() + 1, image));

    private final PixelMove move;

    PixelMoveType(PixelMove move) {
        this.move = move;
    }

    public Pixel movePixel(BufferedImage image, Pixel pixel) {
        return move.movePixel(image, pixel);
    }
}
