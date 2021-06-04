package core;

import javafx.scene.paint.Color;

public class Card {
    private static final Color[] allColors = new Color[]{Color.RED,Color.BLUE,Color.GREEN,Color.YELLOW,Color.PINK,Color.PURPLE,Color.ORANGE,Color.BROWN};
    private static int allColorsUsed = 0;

    private Color color;
    private int rowPos = -1;
    private int colPos = -1;
    private boolean hidden = true;
    private boolean found = false;

    public Color getColor() {
        return color;
    }
    public boolean isHidden() {
        return hidden;
    }
    public boolean isFound() {
        return found;
    }

    public Card(boolean uncolored) {
        if(!uncolored) {
            this.color = allColors[allColorsUsed];
            allColorsUsed++;
        }
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

    public void reveal() {
        this.hidden = false;
    }

    public void hide() {
        this.hidden = true;
    }

    public void find() {
        this.found = true;
    }

    public void resetColors() {
        allColorsUsed = 0;
    }

    @Override
    public String toString() {
        return "Card{" +
                "color=" + color +
                '}';
    }
}
