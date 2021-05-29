package core;

import javafx.scene.paint.Color;

public class Card {
    private static final Color[] allColors = new Color[]{Color.RED,Color.BLUE,Color.GREEN,Color.YELLOW,Color.PINK,Color.PURPLE,Color.ORANGE,Color.BROWN};
    private static int allColorsUsed = 0;

    private Color color;
    private int rowPos = -1;
    private int colPos = -1;

    public Color getColor() {
        return color;
    }

    public Card() {
        this.color = allColors[allColorsUsed];
        allColorsUsed++;
    }

    public Card(Color color) {
        this.color = color;
    }

    public void setPos(int row, int col) {
        this.rowPos = row;
        this.colPos = col;
    }

    public String printPos() {
        if(rowPos == -1 && colPos == -1) {
            return "EMPTY";
        } else {
            return "ROW=" + rowPos + ",COL=" + colPos;
        }
    }

    @Override
    public String toString() {
        return "Card{" +
                "color=" + color +
                '}';
    }
}
