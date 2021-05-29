package core;

import java.util.Arrays;

public class Pair {
    private Card[] content = new Card[2];

    public Card[] getContent() {
        return content;
    }

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
