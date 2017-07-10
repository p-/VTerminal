package com.valkryst.VTerminal.misc;

import com.valkryst.VTerminal.AsciiCharacter;
import com.valkryst.VTerminal.font.Font;
import lombok.Getter;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ColoredImageCache {
    private final Map<AsciiCharacterShell, BufferedImage> cachedImages = new HashMap<>();

    @Getter private final Font font;

    public ColoredImageCache(final Font font) {
        this.font = font;
    }

    public BufferedImage retrieveFromCache(final AsciiCharacter character) {
        final AsciiCharacterShell shell = new AsciiCharacterShell(character, font);

        BufferedImage image = cachedImages.get(shell);

        if (image == null) {
            image = applyColorSwap(shell);
            cachedImages.put(shell, image);
            return image;
        } else {
            return image;
        }
    }

    private static BufferedImage applyColorSwap(final AsciiCharacterShell characterShell) {
        final BufferedImage image = characterShell.getImage();
        final int backgroundRGB = characterShell.getBackgroundColor().getRGB();
        final int foregroundRGB = characterShell.getForegroundColor().getRGB();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                //int alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                // Note that all of the Colors that aren't constructed with new Color(rgb, true),
                // or new Color(0, 0, 0, 0), will have an alpha value of 255. Because of this,
                // I'm ignoring the alpha channel.
                // All incoming images are expected to use a white character with alpha'd background,
                // so we can assume that rgb(0,0,0) is alpha and rgb(255,255,255) is white.
                boolean isTransparent = red == 0;
                isTransparent &= green == 0;
                isTransparent &= blue == 0;

                if (isTransparent) {
                    image.setRGB(x, y, backgroundRGB);
                } else {
                    image.setRGB(x, y, foregroundRGB);
                }
            }
        }

        return image;
    }

    private class AsciiCharacterShell {
        /** The font. */
        @Getter private final Font font;
        /** The character. */
        @Getter private final char character;
        /** The background color. Defaults to black. */
        @Getter private final Color backgroundColor;
        /** The foreground color. Defaults to white. */
        @Getter private final Color foregroundColor;

        public AsciiCharacterShell(final AsciiCharacter character, final Font font) {
            if (character == null) {
                throw new IllegalArgumentException("The AsciiCharacterShell cannot use a null character");
            }

            if (font == null) {
                throw new IllegalArgumentException("The AsciiCharacterShell cannot have a null font.");
            }

            this.font = font;
            this.character = character.getCharacter();
            this.backgroundColor = character.getBackgroundColor();
            this.foregroundColor = character.getForegroundColor();
        }

        public BufferedImage getImage() {
            return font.getCharacterImage(character);
        }

        @Override
        public boolean equals(final Object otherObj) {
            if (otherObj instanceof AsciiCharacterShell == false) {
                return false;
            }

            if (otherObj == this) {
                return true;
            }

            final AsciiCharacterShell otherShell = (AsciiCharacterShell) otherObj;
            boolean isEqual = font.equals(otherShell.getFont());
            isEqual &= character == otherShell.getCharacter();
            isEqual &= backgroundColor.equals(otherShell.getBackgroundColor());
            isEqual &= foregroundColor.equals(otherShell.getForegroundColor());
            return isEqual;
        }

        @Override
        public int hashCode() {
            return Objects.hash(font, character, backgroundColor, foregroundColor);
        }
    }
}
