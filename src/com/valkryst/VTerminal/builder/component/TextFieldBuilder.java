package com.valkryst.VTerminal.builder.component;

import com.valkryst.VTerminal.component.TextArea;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class TextFieldBuilder extends TextAreaBuilder {
    @Override
    public TextArea build() {
        checkState();
        return new TextArea(this);
    }

    /**
     * Checks the current state of the builder.
     *
     * @throws IllegalArgumentException
     *          If the width or max characters is less than one.
     */
    protected void checkState() throws NullPointerException {
        super.checkState();

        if (super.getMaxHorizontalCharacters() < 1) {
            throw new IllegalArgumentException("The maximum characters cannot be less than one.");
        }

        if (super.getMaxHorizontalCharacters() < width) {
            super.setMaxHorizontalCharacters(width);
        }

        if (super.getMaxVerticalCharacters() != 1) {
            super.setMaxVerticalCharacters(1);
        }

        super.setPageUpKeyEnabled(false);
        super.setPageDownKeyEnabled(false);
        super.setUpArrowKeyEnabled(false);
        super.setDownArrowKeyEnabled(false);
        super.setEnterKeyEnabled(false);
    }

    @Override
    public void reset() {
        super.reset();

        super.setMaxVerticalCharacters(1);

        super.setPageUpKeyEnabled(false);
        super.setPageDownKeyEnabled(false);
        super.setUpArrowKeyEnabled(false);
        super.setDownArrowKeyEnabled(false);
        super.setEnterKeyEnabled(false);
    }
}