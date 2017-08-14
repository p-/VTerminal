package com.valkryst.VTerminal.AsciiTileTest;

import com.valkryst.VTerminal.AsciiTile;
import com.valkryst.VTerminal.font.Font;
import com.valkryst.VTerminal.font.FontLoader;
import com.valkryst.VTerminal.misc.ColoredImageCache;
import org.junit.Before;
import org.junit.Test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;

public class DrawTest {
    private AsciiTile character;
    private Font font;

    public DrawTest() throws IOException, URISyntaxException {
        font = FontLoader.loadFontFromJar("Tiles/Nevanda Nethack/bitmap.png", "Tiles/Nevanda Nethack/data.fnt", 1);
    }

    @Before
    public void initializeCharacter() {
        character = new AsciiTile('?');
        character.setBackgroundColor(Color.BLACK);
        character.setForegroundColor(Color.WHITE);
    }

    @Test(expected=NullPointerException.class)
    public void testWithNullGC() {
        character.draw(null, new ColoredImageCache(font), 0, 0);
    }

    @Test(expected=NullPointerException.class)
    public void testWithNullImageCache() {
        final int width = font.getWidth();
        final int height = font.getHeight();
        final BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        character.draw((Graphics2D) temp.getGraphics(), null, 0, 0);
    }

    @Test
    public void testWithValidInputs_withDefaultSettings() {
        final int width = font.getWidth();
        final int height = font.getHeight();
        final BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        character.draw((Graphics2D) temp.getGraphics(), new ColoredImageCache(font), 0, 0);
    }

    @Test
    public void testWithValidInputs_withHiddenChar() {
        final int width = font.getWidth();
        final int height = font.getHeight();
        final BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        character.setHidden(true);
        character.draw((Graphics2D) temp.getGraphics(), new ColoredImageCache(font), 0, 0);
    }

    @Test
    public void testWithValidInputs_withHorizontalFlip() {
        final int width = font.getWidth();
        final int height = font.getHeight();
        final BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        character.setFlippedHorizontally(true);
        character.draw((Graphics2D) temp.getGraphics(), new ColoredImageCache(font), 0, 0);
    }

    @Test
    public void testWithValidInputs_withVerticalFlip() {
        final int width = font.getWidth();
        final int height = font.getHeight();
        final BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        character.setFlippedVertically(true);
        character.draw((Graphics2D) temp.getGraphics(), new ColoredImageCache(font), 0, 0);
    }

    @Test
    public void testWithValidInputs_withUnderline() {
        final int width = font.getWidth();
        final int height = font.getHeight();
        final BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        character.setUnderlined(true);
        character.draw((Graphics2D) temp.getGraphics(), new ColoredImageCache(font), 0, 0);
    }
}
