package core;

import java.util.Arrays;

public class Pair {
    private Card[] content = new Card[2];

    public Card[] getContent() {
        return content;
    }

    /**
     * Creates a new pair, which is an array of two similar cards.
     * @param card the card that will be used to create the pair
     */
    public Pair(Card card) {
        Arrays.fill(content,card);
        content[0] = new Card(card.getColor());
        content[1] = new Card(card.getColor());
    }

    @Override
    public String toString() {
        return "Pair{" +
                "content=" + Arrays.toString(content) +
                '}';
    }
}
