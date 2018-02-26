package com.valkryst.VTerminal.builder;

import com.valkryst.VTerminal.component.Component;
import com.valkryst.VTerminal.palette.ColorPalette;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.awt.Dimension;
import java.awt.Point;

public class ComponentBuilder<C extends Component> {
    /** The position of the component within it's parent. */
    @NonNull private Point position = new Point(0, 0);

    /** The dimensions of the component. */
    @NonNull private Dimension dimensions = new Dimension(1, 1);

    /** The color palette to color the label with. */
    @Getter @Setter @NonNull private ColorPalette colorPalette;

    /** Constructs a new ComponentBuilder. */
    public ComponentBuilder() {
        reset();
    }

    /**
     * Uses the builder to construct a new component.
     *
     * @return
     *         The new component.
     *
     * @throws IllegalStateException
     *          If something is wrong with the builder's state.
     */
    public C build() {
        checkState();
        return null;
    }

    /**
     * Checks the current state of the builder.
     *
     * @throws IllegalArgumentException
     *          If the column or row indices are less than zero.
     *
     * @throws NullPointerException
     *          If the panel is null.
     */
    protected void checkState() throws NullPointerException {
        if (dimensions.width < 1) {
            throw new IllegalArgumentException("You must specify a width of 1 or greater.");
        }

        if (dimensions.height < 1) {
            throw new IllegalArgumentException("You must specify a height of 1 or greater.");
        }
    }

    /** Resets the builder to it's default state. */
    public void reset() {
        position.setLocation(0, 0);
        dimensions.setSize(1, 1);
        colorPalette = new ColorPalette();
    }

    /**
     * Retrieves the x-axis position of the component within it's parent.
     *
     * @return
     *          The x-axis position of the component within it's parent.
     */
    public int getXPosition() {
        return position.x;
    }

    /**
     * Retrieves the y-axis position of the component within it's parent.
     *
     * @return
     *          The y-axis position of the component within it's parent.
     */
    public int getYPosition() {
        return position.y;
    }

    /**
     * Retrieves the width of the component.
     *
     * @return
     *          The width of the component.
     */
    public int getWidth() {
        return dimensions.width;
    }

    /**
     * Retrieves the height of the component.
     *
     * @return
     *          The height of the component.
     */
    public int getHeight() {
        return dimensions.height;
    }

    /**
     * Sets the new position of the component within it's parent.
     *
     * @param point
     *          The new position.
     */
    public void setPosition(final Point point) {
        setPosition(point.x, point.y);
    }

    /**
     * Sets the new position of the component within it's parent.
     *
     * @param x
     *          The x-axis position.
     *
     * @param y
     *          The y-axis position.
     */
    public void setPosition(final int x, final int y) {
        if (x >= 0 && y >= 0) {
            position.setLocation(x, y);
        }
    }

    /**
     * Sets the new x-axis position of the component within it's parent.
     *
     * @param x
     *          The new x-axis position.
     */
    public void setXPosition(final int x) {
        if (x >= 0) {
            position.setLocation(x, position.y);
        }
    }

    /**
     * Sets the new y-axis position of the component within it's parent.
     *
     * @param y
     *          The new y-axis position.
     */
    public void setYPosition(final int y) {
        if (y >= 0) {
            position.setLocation(position.x, y);
        }
    }

    /**
     * Sets the new dimensions of the component.
     *
     * @param dimensions
     *          The new dimensions.
     */
    public void setDimensions(final Dimension dimensions) {
        setDimensions(dimensions.width, dimensions.height);
    }

    /**
     * Sets the new dimensions of the component.
     *
     * @param width
     *          The new width.
     *
     * @param height
     *          The new height.
     */
    public void setDimensions(final int width, final int height) {
        if (width > 0 && height > 0) {
            dimensions.setSize(width, height);
        }
    }

    /**
     * Sets the new width of the component.
     *
     * @param width
     *          The new width.
     */
    public void setWidth(final int width) {
        if (width > 0) {
            dimensions.setSize(width, dimensions.height);
        }
    }

    /**
     * Sets the new height of the component.
     *
     * @param height
     *          The new height.
     */
    public void setHeight(final int height) {
        if (height > 0) {
            dimensions.setSize(dimensions.width, height);
        }
    }
}
