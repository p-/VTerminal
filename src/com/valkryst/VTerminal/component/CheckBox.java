package com.valkryst.VTerminal.component;


import com.valkryst.VTerminal.Screen;
import com.valkryst.VTerminal.Tile;
import com.valkryst.VTerminal.builder.CheckBoxBuilder;
import com.valkryst.VTerminal.palette.ColorPalette;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;

@ToString
public class CheckBox extends Button {
    /** The character to display when the checkbox is not checked. */
    @Getter private char emptyBoxChar;
    /** The character to display when the checkbox is checked. */
    @Getter private char checkedBoxChar;

    /** Whether or not the check box is checked. */
    @Getter private boolean isChecked;

    /**
     * Constructs a new AsciiCheckBox.
     *
     * @param builder
     *         The builder to use.
     *
     * @throws NullPointerException
     *         If the builder is null.
     */
    public CheckBox(final @NonNull CheckBoxBuilder builder) {
        super(builder);

        this.emptyBoxChar = builder.getEmptyBoxChar();
        this.checkedBoxChar = builder.getCheckedBoxChar();

        this.isChecked = builder.isChecked();

        final ColorPalette colorPalette = builder.getColorPalette();

        super.backgroundColor_normal = colorPalette.getCheckBox_defaultBackground();
        super.foregroundColor_normal = colorPalette.getCheckBox_defaultForeground();

        super.backgroundColor_hover = colorPalette.getCheckBox_hoverBackground();
        super.foregroundColor_hover = colorPalette.getCheckBox_hoverForeground();

        super.backgroundColor_pressed = colorPalette.getCheckBox_checkedBackground();
        super.foregroundColor_pressed = colorPalette.getCheckBox_checkedForeground();

        if (isChecked) {
            super.tiles.getTileAt(0, 0).setCharacter(checkedBoxChar);

            for (final Tile tile : super.tiles.getRow(0)) {
                tile.setBackgroundColor(backgroundColor_pressed);
                tile.setForegroundColor(foregroundColor_pressed);
            }
        }
    }

    @Override
    public void createEventListeners(final @NonNull Screen parentScreen) {
        if (super.eventListeners.size() > 0) {
            return;
        }

        final MouseInputListener mouseListener = new MouseInputListener() {
            @Override
            public void mouseDragged(final MouseEvent e) {}

            @Override
            public void mouseMoved(final MouseEvent e) {
                if (intersects(parentScreen.getMousePosition())) {
                    setStateHovered();
                } else {
                    if (isChecked) {
                        setStatePressed();
                    } else {
                        setStateNormal();
                    }
                }
            }

            @Override
            public void mouseClicked(final MouseEvent e) {}

            @Override
            public void mousePressed(final MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (intersects(parentScreen.getMousePosition())) {
                        if (isChecked) {
                            setChecked(false);
                        } else {
                            CheckBox.super.getOnClickFunction().run();
                            setChecked(true);
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(final MouseEvent e) {}

            @Override
            public void mouseEntered(final MouseEvent e) {}

            @Override
            public void mouseExited(final MouseEvent e) {}
        };

        super.eventListeners.add(mouseListener);
    }

    @Override
    public void setColorPalette(final ColorPalette colorPalette, final boolean redraw) {
        if (colorPalette == null) {
            return;
        }

        // Set the instance variables.
        this.colorPalette = colorPalette;

        this.backgroundColor_normal = colorPalette.getCheckBox_defaultBackground();
        this.foregroundColor_normal = colorPalette.getCheckBox_defaultForeground();

        this.backgroundColor_pressed = colorPalette.getCheckBox_checkedBackground();
        this.foregroundColor_pressed = colorPalette.getCheckBox_checkedForeground();

        this.backgroundColor_hover = colorPalette.getCheckBox_hoverBackground();
        this.foregroundColor_hover = colorPalette.getCheckBox_hoverForeground();

        // Determine the colors to color the tiles with.
        final Color backgroundColor;
        final Color foregroundColor;

        if (isChecked) {
            backgroundColor = backgroundColor_pressed;
            foregroundColor = foregroundColor_pressed;
        } else {
            backgroundColor = backgroundColor_normal;
            foregroundColor = foregroundColor_normal;
        }

        for (int y = 0 ; y < super.tiles.getHeight() ; y++) {
            for (int x = 0 ; x < super.tiles.getWidth() ; x++) {
                final Tile tile = super.tiles.getTileAt(x, y);
                tile.setBackgroundColor(backgroundColor);
                tile.setForegroundColor(foregroundColor);
            }
        }

        if (redraw) {
            try {
                redrawFunction.run();
            } catch (final IllegalStateException ignored) {
                /*
                 * If we set the color palette before the screen is displayed, then it'll throw...
                 *
                 *      IllegalStateException: Component must have a valid peer
                 *
                 * We can just ignore it in this case, because the screen will be drawn when it is displayed for
                 * the first time.
                 */
            }
        }
    }

    /**
     * Sets the checked state.
     *
     * @param isChecked
     *          Whether or not the check box is checked.
     */
    public void setChecked(final boolean isChecked) {
        this.isChecked = isChecked;

        if (isChecked) {
            super.tiles.getTileAt(0, 0).setCharacter(checkedBoxChar);
        } else {
            super.tiles.getTileAt(0, 0).setCharacter(emptyBoxChar);
        }

        super.redrawFunction.run();
    }

    @Override
    public void setText(String text) {
        if (text == null) {
            text = "";
        }

        for (int x = 0 ; x < tiles.getWidth() ; x++) {
            if (x == 0 || x == 1) {
                continue;
            }

            if (x <= text.length()) {
                // Because we skip the first two tiles (checkbox & space after it), we have to use -2 to
                // compensate.
                tiles.getTileAt(x, 0).setCharacter(text.charAt(x - 2));
            } else {
                tiles.getTileAt(x, 0).setCharacter(' ');
            }
        }
    }
}
