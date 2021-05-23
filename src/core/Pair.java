package core;

import java.util.Arrays;

public class Pair {
    private Card[] content = new Card[2];

    public Card[] getContent() {
        return content;
    }

    public Pair(Card card) {
        Arrays.fill(content,card);
    }

    @Override
    public String toString() {
        return "Pair{" +
                "content=" + Arrays.toString(content) +
                '}';
    }
}
