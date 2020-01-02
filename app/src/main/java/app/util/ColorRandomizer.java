package app.util;

import javafx.scene.paint.Color;

import java.util.Random;

public class ColorRandomizer {

    public static String randomColor() {
        Random random = new Random();
        return Color.color(random.nextDouble()*0.5 + 0.25, random.nextDouble()*0.5 + 0.25, random.nextDouble()*0.5 + 0.25)
                .toString();
    }
}
