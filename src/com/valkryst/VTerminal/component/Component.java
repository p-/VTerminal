package com.valkryst.VTerminal.component;

import com.valkryst.VRadio.Radio;
import com.valkryst.VTerminal.AsciiCharacter;
import com.valkryst.VTerminal.AsciiString;
import com.valkryst.VTerminal.AsciiTile;
import com.valkryst.VTerminal.Panel;
import com.valkryst.VTerminal.builder.component.ComponentBuilder;
import com.valkryst.VTerminal.font.Font;
import com.valkryst.VTerminal.misc.IntRange;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@ToString
public class Component {
    /** The ID. Not guaranteed to be unique. */
    @Getter private String id;

    /** The x-axis (column) coordinate of the top-left character. */
    @Getter private int columnIndex;
    /** The y-axis (row) coordinate of the top-left character. */
    @Getter private int rowIndex;

    /** The width, in characters. */
    @Getter private int width;
    /** The height, in characters. */
    @Getter private int height;

    /** Whether or not the component is currently the target of the user's input. */
    @Getter protected boolean isFocused = false;

    /** The bounding box. */
    @Getter private Rectangle boundingBox = new Rectangle();

    /** The strings representing the character-rows of the component. */
    @Getter private AsciiString[] strings;

    /** The radio to transmit events to. */
    @Getter private Radio<String> radio;

    /** The screen that the component is on. */
    @Getter @Setter private Screen screen;

    /** The event listeners. */
    @Getter private final Set<EventListener> eventListeners = new HashSet<>();

    /**
     * Constructs a new AsciiComponent.
     *
     * @param builder
     *         The builder to use.
     *
     * @param width
     *         The width, in characters.
     *
     * @param height
     *         The height, in characters.
     *
     * @throws NullPointerException
     *         If the builder is null.
     */
    public Component(final @NonNull ComponentBuilder builder, final int width, final int height) {
        this(builder.getColumnIndex(), builder.getRowIndex(), width, height);
        this.id = builder.getId();
    }


    /**
     * Constructs a new AsciiComponent.
     *
     * @param columnIndex
     *         The x-axis (column) coordinate of the top-left character.
     *
     * @param rowIndex
     *         The y-axis (row) coordinate of the top-left character.
     *
     * @param width
     *         The width, in characters.
     *
     * @param height
     *         The height, in characters.
     */
    public Component(final int columnIndex, final int rowIndex, final int width, final int height) {
        if (columnIndex < 0) {
            throw new IllegalArgumentException("You must specify a columnIndex of 0 or greater.");
        }

        if (rowIndex < 0) {
            throw new IllegalArgumentException("You must specify a rowIndex of 0 or greater.");
        }

        if (width < 1) {
            throw new IllegalArgumentException("You must specify a width of 1 or greater.");
        }

        if (height < 1) {
            throw new IllegalArgumentException("You must specify a height of 1 or greater.");
        }

        this.id = "No ID Set. Random ID = " + ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
        this.columnIndex = columnIndex;
        this.rowIndex = rowIndex;
        this.width = width;
        this.height = height;

        boundingBox.setLocation(columnIndex, rowIndex);
        boundingBox.setSize(width, height);

        strings = new AsciiString[height];

        for (int row = 0 ; row < height ; row++) {
            strings[row] = new AsciiString(width);
        }
    }

    /**
     * Creates all required event listeners for the component.
     *
     * @param panel
     *         The panel on which the Component is being drawn.
     *
     * @throws NullPointerException
     *         If the panel is null.
     */
    public void createEventListeners(final @NonNull Panel panel) {
        if (eventListeners.size() > 0) {
            return;
        }

        final Font font = panel.getImageCache().getFont();
        final int fontWidth = font.getWidth();
        final int fontHeight = font.getHeight();

        final MouseListener mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    isFocused = intersects(e, fontWidth, fontHeight);
                }
            }

            @Override
            public void mousePressed(final MouseEvent e) {}

            @Override
            public void mouseReleased(final MouseEvent e) {}

            @Override
            public void mouseEntered(final MouseEvent e) {}

            @Override
            public void mouseExited(final MouseEvent e) {}
        };

        eventListeners.add(mouseListener);
    }

    /**
     * Draws the component on the specified screen.
     *
     * @param screen
     *         The screen to draw on.
     *
     * @throws NullPointerException
     *         If the screen is null.
     */
    public void draw(final @NonNull Screen screen) {
        for (int row = 0 ; row < strings.length ; row++) {
            screen.write(strings[row], columnIndex, rowIndex + row);
        }
    }

    /** Attempts to transmit a "DRAW" event to the assigned Radio. */
    public void transmitDraw() {
        if (radio != null) {
            radio.transmit("DRAW");
        }
    }

    /** Converts every AsciiCharacter of every AsciiString into an AsciiTile. */
    public void convertAsciiCharactersToAsciiTiles() {
        for (final AsciiString string : strings) {
            final AsciiCharacter[] characters = string.getCharacters();

            for (int i = 0 ; i < characters.length ; i++) {
                string.setCharacter(i, new AsciiTile(characters[i]));
            }
        }
    }

    /** Converts every AsciiTile of every AsciiString into an AsciiCharacter. */
    public void convertAsciiTilesToAsciiCharacters() {
        for (final AsciiString string : strings) {
            final AsciiCharacter[] characters = string.getCharacters();

            for (int i = 0 ; i < characters.length ; i++) {
                if (characters[i] instanceof AsciiTile) {
                    string.setCharacter(i, new AsciiCharacter(characters[i]));
                }
            }
        }
    }

    /**
     * Determines if the specified component intersects with this component.
     *
     * @param otherComponent
     *         The component to check intersection with.
     *
     * @return
     *         Whether or not the components intersect.
     *
     * @throws NullPointerException
     *         If the other component is null.
     */
    public boolean intersects(final @NonNull Component otherComponent) {
        return boundingBox.intersects(otherComponent.getBoundingBox());

    }

    /**
     * Determines if the specified point intersects with this component.
     *
     * @param pointX
     *         The x-axis (column) coordinate.
     *
     * @param pointY
     *         The y-axis (row) coordinate.
     *
     * @return
     *         Whether or not the point intersects with this component.
     */
    public boolean intersects(final int pointX, final int pointY) {
        boolean intersects = pointX >= columnIndex;
        intersects &= pointX < (boundingBox.getWidth() + columnIndex);
        intersects &= pointY >= rowIndex;
        intersects &= pointY < (boundingBox.getHeight() + rowIndex);

        return intersects;
    }

    /**
     * Determines whether or not the specified mouse event is at a point that intersects this component.
     *
     * @param event
     *         The event.
     *
     * @param fontWidth
     *         The width of the font being used to draw the component's characters.
     *
     * @param fontHeight
     *         The height of the font being used to draw the component's characters.
     *
     * @return
     *         Whether or not the mouse event is at a point that intersects this component.
     *
     * @throws NullPointerException
     *         If the event is null.
     */
    public boolean intersects(final @NonNull MouseEvent event, final int fontWidth, final int fontHeight) {
        final int mouseX = event.getX() / fontWidth;
        final int mouseY = event.getY() / fontHeight;
        return intersects(mouseX, mouseY);
    }

    /**
     * Determines whether or not the specified position is within the bounds of the component.
     *
     * @param columnIndex
     *         The x-axis (column) coordinate.
     *
     * @param rowIndex
     *         The y-axis (row) coordinate.
     *
     * @return
     *         Whether or not the specified position is within the bounds of the component.
     */
    public boolean isPositionValid(final int columnIndex, final int rowIndex) {
        if (rowIndex < 0 || rowIndex > boundingBox.getHeight() - 1) {
            return false;
        }

        if (columnIndex < 0 || columnIndex > boundingBox.getWidth() - 1) {
            return false;
        }

        return true;
    }

    /**
     * Enables the blink effect for every character.
     *
     * @param millsBetweenBlinks
     *         The amount of time, in milliseconds, before the blink effect can occur.
     *
     * @param radio
     *         The Radio to transmit a DRAW event to whenever a blink occurs.
     *
     * @throws NullPointerException
     *        If the radio is null.
     */
    public void enableBlinkEffect(final short millsBetweenBlinks, final @NonNull Radio<String> radio) {
        for (final AsciiString s: strings) {
            for (final AsciiCharacter c : s.getCharacters()) {
                c.enableBlinkEffect(millsBetweenBlinks, radio);
            }
        }
    }

    /** Resumes the blink effect for every character. */
    public void resumeBlinkEffect() {
        for (final AsciiString s: strings) {
            for (final AsciiCharacter c : s.getCharacters()) {
                c.resumeBlinkEffect();
            }
        }
    }

    /** Pauses the blink effect for every character. */
    public void pauseBlinkEffect() {
        for (final AsciiString s: strings) {
            for (final AsciiCharacter c : s.getCharacters()) {
                c.pauseBlinkEffect();
            }
        }
    }

    /** Disables the blink effect for every character. */
    public void disableBlinkEffect() {
        for (final AsciiString s: strings) {
            for (final AsciiCharacter c : s.getCharacters()) {
                c.disableBlinkEffect();
            }
        }
    }

    /**
     * Retrieves the string corresponding to a row.
     *
     * @param rowIndex
     *        The row index.
     *
     * @return
     *        The string.
     */
    public AsciiString getString(final int rowIndex) {
        return strings[rowIndex];
    }

    /** Sets all characters to be redrawn. */
    public void setAllCharactersToBeRedrawn() {
        for (final AsciiString string : strings) {
            string.setAllCharactersToBeRedrawn();
        }
    }

    /**
     * Sets every character, on the Screen that the component
     * resides on, at the component's current location to be
     * redrawn.
     *
     * This should only be called when the component is moved
     * on-screen or resized.
     */
    private void setLocationOnScreenToBeRedrawn() {
        for (int y = rowIndex ; y <= rowIndex + height ; y++) {
            screen.getString(y).setCharacterRangeToBeRedrawn(new IntRange(columnIndex, columnIndex + width));
        }
    }

    /**
     * Retrieves the AsciiCharacter at a specific position.
     *
     * @param columnIndex
     *        The x-axis (column) coordinate of the position.
     *
     * @param rowIndex
     *        The y-axis (row) coordinate of the position.
     *
     * @return
     *        The AsciiCharacter.
     *
     * @throws IllegalArgumentException
     *         If the position is invalid.
     */
    public AsciiCharacter getCharacterAt(final int columnIndex, final int rowIndex) {
        if (isPositionValid(columnIndex, rowIndex)) {
            return strings[rowIndex].getCharacters()[columnIndex];
        }

        throw new IllegalArgumentException("The position (" + columnIndex + " columnIndex, " + rowIndex + " rowIndex) is invalid.");
    }

    /**
     * Sets a new value for the columnIndex.
     *
     * @param columnIndex
     *         The new x-axis (column) coordinate of the top-left
     *         character of the component.
     *
     * @throws IllegalArgumentException
     *         If the columnIndex is < 0.
     */
    public void setColumnIndex(final int columnIndex) {
        if (columnIndex < 0) {
            throw new IllegalArgumentException("The columnIndex cannot be < 0.");
        }

        setLocationOnScreenToBeRedrawn();
        this.columnIndex = columnIndex;
        boundingBox.setLocation(columnIndex, rowIndex);
        setAllCharactersToBeRedrawn();
    }

    /**
     * Sets a new value for the rowIndex.
     *
     * @param rowIndex
     *         The y-axis (row) coordinate of the top-left character of
     *         the component.
     *
     * @throws IllegalArgumentException
     *         If the rowIndex is < 0.
     */
    public void setRowIndex(final int rowIndex) {
        if (rowIndex < 0) {
            throw new IllegalArgumentException("The rowIndex cannot be < 0.");
        }

        setLocationOnScreenToBeRedrawn();
        this.rowIndex = rowIndex;
        boundingBox.setLocation(columnIndex, rowIndex);
        setAllCharactersToBeRedrawn();
    }

    /**
     * Sets a new value for the width.
     *
     * @param width
     *         The new width, in characters, of the component.
     *
     * @throws IllegalArgumentException
     *         If the width is < 0 or the width is < columnIndex.
     */
    public void setWidth(final int width) {
        if (width < 0) {
            throw new IllegalArgumentException("The width cannot be < 0.");
        }

        if (width < columnIndex) {
            throw new IllegalArgumentException("The width cannot be < columnIndex,");
        }

        setLocationOnScreenToBeRedrawn();
        this.width = width;
        boundingBox.setSize(width, height);
        setAllCharactersToBeRedrawn();
    }

    /**
     * Sets a new value for the height.
     *
     * @param height
     *         The new height, in characters, of the component.
     *
     * @throws java.lang.IllegalArgumentException
     *         If the height is < 0 or the height is < rowIndex.
     */
    public void setHeight(final int height) {
        if (height < 0) {
            throw new IllegalArgumentException("The height cannot be < 0.");
        }

        if (height < rowIndex) {
            throw new IllegalArgumentException("The height cannot be < rowIndex,");
        }

        setLocationOnScreenToBeRedrawn();
        this.height = height;
        boundingBox.setSize(width, height);
        setAllCharactersToBeRedrawn();
    }

    /**
     * Sets a new radio.
     *
     * @param radio
     *         The new radio.
     *
     * @throws NullPointerException
     *         If the radio is null.
     */
    public void setRadio(final @NonNull Radio<String> radio) {
        this.radio = radio;
    }
}