package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class OrkImpact implements UniqueEffect {
    private boolean orkFired = false;

    @Override
    public EffectTime getEffectTime() {
        return EffectTime.AfterCombat;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        if (!orkFired) {
            Card flyingOrk = new CardMonster("Flying Ork", 35, CardType.Air, Rarity.Epic, null, MonsterTag.Ork);
            flyingOrk.setOwnerName(self.getOwnerName());
            selfDeck.addCard(flyingOrk);
            orkFired = true;
            return "An Ork was launched through the air";
        }
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
