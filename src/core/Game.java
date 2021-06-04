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

    public void buildDeck() {
        if(!cards.isEmpty())
            for (Card card : cards) {
                pairs.add(new Pair(card));
            }
    }

    public void generateCards(int amount) {
        for(int i = 0; i < amount; i++) {
            cards.add(new Card(false));
        }
    }

    public void shufflePairs() {
        Collections.shuffle(this.pairs);
    }

    public void appendScore(int value) {
        this.score = Math.max(0,this.score+=value);
    }
}
