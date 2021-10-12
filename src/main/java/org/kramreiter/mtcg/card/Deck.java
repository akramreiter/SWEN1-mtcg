package org.kramreiter.mtcg.card;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class Deck {
    protected Card[] cards;
    protected String playerName;

    public Deck(String[] sourceCards, String player) {
        playerName = player;
        List<Card> l = new ArrayList<>();
        for (String card : sourceCards) {
            l.add(CardFactory.getCard(card));
        }
        cards = l.toArray(new Card[0]);
    }

    protected Deck(String player) {
        cards = new Card[0];
        playerName = player;
    }

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
            list.add(c.clone());
        }
        Deck deck;
        try {
            deck = (Deck) super.clone();
        } catch (CloneNotSupportedException e) {
            deck = new Deck(playerName);
        }
        deck.cards = list.toArray(new Card[0]);
        return deck;
    }
}