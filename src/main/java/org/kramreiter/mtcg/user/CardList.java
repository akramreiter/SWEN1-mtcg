package org.kramreiter.mtcg.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;
import org.kramreiter.mtcg.card.CardFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class CardList {
    @JsonAlias("card")
    private Map<String,Integer> cardIds = new HashMap<>();

    public boolean addCard(String cardId) {
        if (CardFactory.getInstance().getCard(cardId) != null) {
            if (cardIds.containsKey(cardId)) {
                cardIds.put(cardId, cardIds.get(cardId) + 1);
            } else {
                cardIds.put(cardId, 1);
            }
            return true;
        }
        return false;
    }
    public boolean addCards(String[] cardIdsToAdd) {
        boolean allSuccessful = true;
        for (String cardId : cardIdsToAdd) {
            if (CardFactory.getInstance().getCard(cardId) != null) {
                if (cardIds.containsKey(cardId)) {
                    cardIds.put(cardId, cardIds.get(cardId) + 1);
                } else {
                    cardIds.put(cardId, 1);
                }
            } else {
                allSuccessful = false;
            }
        }
        return allSuccessful;
    }
    public boolean removeCard(String cardId) {
        if (CardFactory.getInstance().getCard(cardId) != null) {
            if (cardIds.containsKey(cardId)) {
                if (cardIds.get(cardId) > 1) {
                    cardIds.put(cardId, cardIds.get(cardId) - 1);
                } else {
                    cardIds.remove(cardId);
                }
                return true;
            }
        }
        return false;
    }

    public String[] toArray() {
        ArrayList<String> output = new ArrayList<>();
        for (String cardId : cardIds.keySet()) {
            for (int i = 0; i < cardIds.get(cardId); i++) {
                output.add(cardId);
            }
        }
        return output.toArray(new String[0]);
    }

    public Map<String, Integer> get() {
        return cardIds;
    }

    public int size() {
        int size = 0;
        for (String s : cardIds.keySet()) {
            size += cardIds.get(s);
        }
        return size;
    }
}
