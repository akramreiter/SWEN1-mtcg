package org.kramreiter.mtcg.card.effect;

import org.kramreiter.mtcg.card.*;

public class GearUp implements UniqueEffect {
    private static final int SPEARSTR = 40;
    private boolean available = true;

    @Override
    public EffectTime getEffectTime() {
        return EffectTime.AfterCombat;
    }

    @Override
    public String executeEffect(Card self, Card opponent, Deck selfDeck, Deck opponentDeck, CombatOutcome prevOutcome) {
        if (!available) return null;
        selfDeck.removeCard(self);
        Card spear = new CardSpell("Spear of Justice", SPEARSTR, CardType.Normal, Rarity.Legendary, null);
        spear.setOwnerName(self.getOwnerName());
        selfDeck.addCards(new Card[] {
                spear.clone(),
                spear.clone(),
                spear
        });
        available = false;
        return "Spears have been added to the armory";
    }

    @Override
    public String getDescription() {
        return "Effect: Gear Up\nAfter combat -> add 3 Spears of Justice (strength " + SPEARSTR + ") to deck (once per game)\nA spear for every occasion";
    }

    @Override
    public String getName() {
        return "Gear Up";
    }
}
