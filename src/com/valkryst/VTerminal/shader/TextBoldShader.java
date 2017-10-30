package com.valkryst.VTerminal.shader;

import lombok.NonNull;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class TextBoldShader implements Shader {
    @Override
    public BufferedImage run(final @NonNull BufferedImage image) {
        try {
            // Get the character image:
            final Color[] colors = getBackgroundAndForegroundRGBColor(image);
            final BufferedImage charImage = swapColor(image, colors[0], new Color(0, 0, 0, 0));

            // Combine image and background
            final BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
            final Graphics2D gc = (Graphics2D) result.getGraphics();

            gc.setColor(colors[0]);
            gc.fillRect(0, 0, result.getWidth(), result.getHeight());
            gc.drawImage(charImage, 1, 0, null);
            gc.drawImage(charImage, 0, 0, null);
            gc.dispose();

            return result;
        } catch (final IllegalStateException e) {
            return image;
        }
    }

    @Override
    public Shader copy() {
        return new TextBoldShader();
    }
}
