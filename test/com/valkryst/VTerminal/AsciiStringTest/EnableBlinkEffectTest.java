package com.valkryst.VTerminal.AsciiStringTest;

import com.valkryst.VRadio.Radio;
import com.valkryst.VTerminal.AsciiString;
import com.valkryst.VTerminal.misc.IntRange;
import org.junit.Before;
import org.junit.Test;

import java.awt.Color;

public class EnableBlinkEffectTest {
    private AsciiString string;

    @Before
    public void initializeString() {
        string = new AsciiString("ABCDEFGHJIKLMNOP");
        string.setBackgroundColor(Color.BLACK);
        string.setForegroundColor(Color.WHITE);
    }

    @Test
    public void toAll_withValidInput() {
        string.enableBlinkEffect((short) 1000, new Radio<>());
    }

    @Test(expected=NullPointerException.class)
    public void toAll_withNullRadio() {
        string.enableBlinkEffect((short) 1000, new Radio<>());
    }

    @Test
    public void toRange_withValidInput() {
        final IntRange range = new IntRange(0, 3);
        string.enableBlinkEffect((short) 1000, new Radio<>(), range);
    }

    @Test
    public void toRange_withNullRadio() {
        final IntRange range = new IntRange(0, 3);
        string.enableBlinkEffect((short) 1000, null, range);
    }
}
