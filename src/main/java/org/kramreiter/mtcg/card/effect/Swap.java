package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

import java.util.Arrays;
import java.util.List;

public class Swap implements UniqueEffect {

    @Override
    public EffectTime getEffectTime() {
        return EffectTime.BeforeCombat;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        List<Card> selfList = Arrays.asList(selfDeck.getCards());
        List<Card> opponentList = Arrays.asList(opponentDeck.getCards());
        if (selfList.size() > 1 && opponentList.size() > 1) {
            int selfIndex = getRandomIndex(selfList, self);
            int opponentIndex = getRandomIndex(opponentList, opponent);
            Card selfCard = selfDeck.removeCard(selfIndex);
            Card opponentCard = opponentDeck.removeCard(opponentIndex);
            selfDeck.addCard(opponentCard);
            opponentDeck.addCard(selfCard);
            return "2 Cards changed sides";
        }
        return null;
    }

    private int getRandomIndex(List<Card> deck, Card self) {
        int index = (int) (Math.random() * (deck.size() - 1));
        if (index >= deck.indexOf(self)) {
            index++;
        }
        return index;
    }

    @Override
    public String getDescription() {
        return "Effect: Swap\nBefore combat -> 2 random cards other than the ones in combat will be swapped\nWe do a little trolling";
    }

    @Override
    public String getName() {
        return "Swap";
    }
}
