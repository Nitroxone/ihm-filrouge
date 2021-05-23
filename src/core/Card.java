package core;

import javafx.scene.paint.Color;

public class Card {
    private static final Color[] allColors = new Color[]{Color.RED,Color.BLUE,Color.GREEN,Color.YELLOW,Color.PINK,Color.PURPLE};
    private static int allColorsUsed = 0;

    private Color color;

    public Color getColor() {
        return color;
    }

    public Card() {
        this.color = allColors[allColorsUsed];
        allColorsUsed++;
    }

    @Override
    public String toString() {
        return "Card{" +
                "color=" + color +
                '}';
    }
}
