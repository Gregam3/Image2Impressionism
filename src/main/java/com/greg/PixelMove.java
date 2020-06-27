package com.greg;

import java.awt.image.BufferedImage;

@FunctionalInterface
public interface PixelMove {
    Pixel movePixel(BufferedImage image, Pixel pixel);
}
