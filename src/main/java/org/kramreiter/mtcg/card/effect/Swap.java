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
            int selfIndex = (int) (Math.random() * (selfList.size()));
            if (selfIndex >= selfList.indexOf(self)) {
                selfIndex++;
            }
            int opponentIndex = (int) (Math.random() * (opponentList.size()));
            if (opponentIndex >= opponentList.indexOf(opponent)) {
                opponentIndex++;
            }
            Card selfCard = selfDeck.removeCard(selfIndex);
            Card opponentCard = opponentDeck.removeCard(opponentIndex);
            selfDeck.addCard(opponentCard);
            opponentDeck.addCard(selfCard);
            return "2 Cards changed sides";
        }
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
