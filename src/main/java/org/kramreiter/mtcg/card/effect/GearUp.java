package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class GearUp implements UniqueEffect {

    @Override
    public EffectTime getEffectTime() {
        return EffectTime.AfterCombat;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        selfDeck.removeCard(self);
        Card spear = new CardSpell("Spear of Justice", 40, CardType.Normal, true, Rarity.Legendary, null);
        selfDeck.addCards(new Card[] {
                spear.clone(),
                spear.clone(),
                spear
        });
        return "Spears have been added to the armory";
    }

    @Override
    public String getDescription() {
        return "Effect: Gear Up\nAfter combat -> add 3 Spears of Justice (strength 40) to deck\nA spear for every occasion";
    }
}
