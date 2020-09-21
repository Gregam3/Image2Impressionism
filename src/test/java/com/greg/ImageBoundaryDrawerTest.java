package com.greg;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ImageBoundaryDrawerTest {
    private static final String TEST_RESOURCES_LOCATION = "src/test/resources/";

    private void assertImagesMatch(String imageFile, String imageFileFinished) {
        Optional<BufferedImage> outputImageResult = ImageBoundaryDrawer.drawImageBoundaries(
                new File(TEST_RESOURCES_LOCATION + imageFile + ".png")
        );

        if (!outputImageResult.isPresent()) {
            Assertions.fail("No output image was returned");
        }

        BufferedImage outputImage = outputImageResult.get();

        BufferedImage correctOutputImage = ImageBoundaryDrawer.getImageFromFile(TEST_RESOURCES_LOCATION + imageFileFinished + ".png");

        try {
            assert Arrays.equals(ImageUtil.getBytesFromImage(correctOutputImage), ImageUtil.getBytesFromImage(outputImage));
        } catch (IOException e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void testIfSingleLevelDepthFlagsAreTracedAsExpected() {
        assertImagesMatch("shitty_palau_flag", "shitty_palau_flag_finished");
        assertImagesMatch("ukraine_flag", "ukraine_flag_finished");
        assertImagesMatch("shitty_comoros_flag", "shitty_comoros_flag_finished");
        assertImagesMatch("shitty_finnish_flag", "shitty_finnish_flag_finished");
    }

    @Test
    public void testIfMultiLevelDepthImagesAreTracedAsExpected() {
        assertImagesMatch("three_levels_deep_image", "three_level_deep_image_finished");
    }
}