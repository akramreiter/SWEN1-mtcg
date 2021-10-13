package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class GearUp implements UniqueEffect {
    private static final int SPEARSTR = 40;

    @Override
    public EffectTime getEffectTime() {
        return EffectTime.AfterCombat;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        selfDeck.removeCard(self);
        Card spear = new CardSpell("Spear of Justice", SPEARSTR, CardType.Normal, Rarity.Legendary, null);
        selfDeck.addCards(new Card[] {
                spear.clone(),
                spear.clone(),
                spear
        });
        return "Spears have been added to the armory";
    }

    @Override
    public String getDescription() {
        return "Effect: Gear Up\nAfter combat -> add 3 Spears of Justice (strength " + SPEARSTR + ") to deck\nA spear for every occasion";
    }

    @Override
    public String getName() {
        return "Gear Up";
    }
}
