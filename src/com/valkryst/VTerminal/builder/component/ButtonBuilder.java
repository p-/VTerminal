package com.valkryst.VTerminal.builder.component;

import com.valkryst.VTerminal.component.Button;
import com.valkryst.VTerminal.misc.ColorFunctions;
import com.valkryst.VTerminal.misc.JSONFunctions;
import lombok.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.Color;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class ButtonBuilder extends ComponentBuilder<Button> {
    /** The text to display on the button. */
    @NonNull private String text;

    /** The background color for when the button is in the normal state. */
    @NonNull private Color backgroundColor_normal;
    /** The foreground color for when the button is in the normal state. */
    @NonNull private Color foregroundColor_normal;

    /** The background color for when the button is in the hover state. */
    @NonNull private Color backgroundColor_hover;
    /** The foreground color for when the button is in the hover state. */
    @NonNull private Color foregroundColor_hover;

    /** The background color for when the button is in the pressed state. */
    @NonNull private Color backgroundColor_pressed;
    /** The foreground color for when the button is in the pressed state. */
    @NonNull private Color foregroundColor_pressed;

    /** The function to run when the button is clicked. */
    private Runnable onClickFunction;

    @Override
    public Button build() {
        checkState();

        super.width = text.length();
        super.height = 1;

        return new Button(this);
    }

    /** Resets the builder to it's default state. */
    public void reset() {
        super.reset();

        text = "";

        backgroundColor_normal = new Color(45, 45, 45, 255);
        foregroundColor_normal = new Color(0xFF2DBEFF, true);

        backgroundColor_hover = new Color(0xFF2DFF63, true);
        foregroundColor_hover = ColorFunctions.shade(backgroundColor_hover, 0.5);

        backgroundColor_pressed = ColorFunctions.shade(backgroundColor_hover, 0.25);
        foregroundColor_pressed = ColorFunctions.shade(foregroundColor_hover, 0.25);

        onClickFunction = () -> {};
    }

    @Override
    public void parseJSON(final @NonNull JSONObject jsonObject) {
        reset();
        super.parseJSON(jsonObject);


        final String text = (String) jsonObject.get("text");

        final Color backgroundColor_normal = JSONFunctions.loadColorFromJSON((JSONArray) jsonObject.get("backgroundColor_normal"));
        final Color foregroundColor_normal = JSONFunctions.loadColorFromJSON((JSONArray) jsonObject.get("foregroundColor_normal"));

        final Color backgroundColor_hover = JSONFunctions.loadColorFromJSON((JSONArray) jsonObject.get("backgroundColor_hover"));
        final Color foregroundColor_hover = JSONFunctions.loadColorFromJSON((JSONArray) jsonObject.get("foregroundColor_hover"));

        final Color backgroundColor_pressed = JSONFunctions.loadColorFromJSON((JSONArray) jsonObject.get("backgroundColor_pressed"));
        final Color foregroundColor_pressed = JSONFunctions.loadColorFromJSON((JSONArray) jsonObject.get("foregroundColor_pressed"));


        if (text != null) {
            this.text = text;
        }



        if (backgroundColor_normal != null) {
            this.backgroundColor_normal = backgroundColor_normal;
        }

        if (foregroundColor_normal != null) {
            this.foregroundColor_normal = foregroundColor_normal;
        }



        if (backgroundColor_hover != null) {
            this.backgroundColor_hover = backgroundColor_hover;
        }

        if (foregroundColor_hover != null) {
            this.foregroundColor_hover = foregroundColor_hover;
        }



        if (backgroundColor_pressed != null) {
            this.backgroundColor_pressed = backgroundColor_pressed;
        }

        if (foregroundColor_pressed != null) {
            this.backgroundColor_pressed = backgroundColor_pressed;
        }
    }
}
