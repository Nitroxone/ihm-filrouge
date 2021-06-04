package core;

import java.util.ArrayList;
import java.util.Collections;

public class Game {
    private ArrayList<Pair> pairs = new ArrayList<>();
    private ArrayList<Card> cards = new ArrayList<>();
    private int score = 0;

    public ArrayList<Pair> getPairs() {
        return pairs;
    }
    public ArrayList<Card> getCards() {
        return cards;
    }
    public int getScore() {
        return score;
    }

    /**
     * Generates a new deck (a pair of cards set), based on the amount of cards contained in the cards attribute.
     */
    public void buildDeck() {
        if(!cards.isEmpty())
            for (Card card : cards) {
                pairs.add(new Pair(card));
            }
    }

    /**
     * Generates a new set of cards.
     * @param amount the amount of cards to be generated
     */
    public void generateCards(int amount) {
        for(int i = 0; i < amount; i++) {
            cards.add(new Card(false));
        }
    }

    /**
     * Shuffles the pairs of the current game.
     */
    public void shufflePairs() {
        Collections.shuffle(this.pairs);
    }

    /**
     * Updates the current score with the desired value.
     * @param value the value, positive or negative, that will be appended to the current score.
     */
    public void appendScore(int value) {
        this.score = Math.max(0,this.score+=value);
    }

    /**
     * Sets the current score to 0.
     */
    public void resetScore() {
        this.score = 0;
    }
}
