package org.kramreiter.mtcg.card;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Deck {
    @Getter
    protected Card[] cards = new Card[0];
    public void addCard(Card c) {
        List<Card> l = Arrays.asList(cards);
        l.add(c);
        cards = l.toArray(new Card[0]);
    }
    public void addCards(Card[] c) {
        List<Card> l = Arrays.asList(cards);
        l.addAll(Arrays.asList(c));
        cards = l.toArray(new Card[0]);
    }
    public Card removeCard(int index) {
        List<Card> l = Arrays.asList(cards);
        Card out = l.remove(index);
        cards = l.toArray(new Card[0]);
        return out;
    }
    public boolean removeCard(Card remove) {
        List<Card> l = Arrays.asList(cards);
        boolean out = l.remove(remove);
        cards = l.toArray(new Card[0]);
        return out;
    }

    public Deck clone() {
        List<Card> list = new ArrayList<>();
        for (Card c : getCards()) {
            list.add(c.clone())
        }
    }
}