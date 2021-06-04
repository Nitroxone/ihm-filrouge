package core;

import javafx.scene.paint.Color;

public class Card {
    /**
     * Contains all the colors available in the game.
     */
    private static final Color[] allColors = new Color[]{Color.RED,Color.BLUE,Color.GREEN,Color.YELLOW,Color.PINK,Color.PURPLE,Color.ORANGE,Color.BROWN};
    /**
     * The value corresponds to a position in the <b>allColors</b> array, allowing the system to iterate over it when generating cards.
     */
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

    /**
     * Creates a new card. The color is picked automatically. The attribute <b>uncolored</b> can be used to create an "empty" card, that can be used to call the <b>resetColors</b> method.
     * @param uncolored indicates if the card should be used in the game or not
     */
    public Card(boolean uncolored) {
        if(!uncolored) {
            this.color = allColors[allColorsUsed];
            allColorsUsed++;
        }
    }

    /**
     * Creates a new card of the desired color.
     * @param color the color that will be applied to the card.
     */
    public Card(Color color) {
        this.color = color;
    }

    /**
     * Sets the card's position.
     * @param row the grid row position
     * @param col the grid column position
     */
    public void setPos(int row, int col) {
        this.rowPos = row;
        this.colPos = col;
    }

    /**
     * Returns the position of the card.
     * @return a String that contains the position of the card, format : <b>ROW=x,COL=y</b>. If the card has no position set, returns <b>EMPTY</b>.
     */
    public String printPos() {
        if(rowPos == -1 && colPos == -1) {
            return "EMPTY";
        } else {
            return "ROW=" + rowPos + ",COL=" + colPos;
        }
    }

    /**
     * Reveals the card by switching the <b>hidden</b> attribute to false.
     */
    public void reveal() {
        this.hidden = false;
    }

    /**
     * Hides the card by switching the <b>hidden</b> attribute to true.
     */
    public void hide() {
        this.hidden = true;
    }

    /**
     * Finds the card by switching the <b>found</b> attribute to true.
     */
    public void find() {
        this.found = true;
    }

    /**
     * Resets the <b>allColorsUsed</b> attribute. That allows the system to generate a new set of cards from scratch, and prevent an <i>IndexOutOfBoundsException</i>.
     */
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
