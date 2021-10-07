package org.kramreiter.mtcg;

import org.kramreiter.mtcg.card.*;

public class Launch {
    public static void main(String[] args) {
        System.out.println("Hello World");

        Card knight = new CardMonster("Basic Boring Human Knight", 50, 0, false, 3, null, 0);
        System.out.println(knight);

        Card fireball = new CardSpell("Fireball", 40, CardType.Fire, true, Rarity.Rare, null);
        System.out.println(fireball);
        System.out.println("effectiveness of " + fireball.getName() + " against " + knight.getName() + ": " + RuleManager.computeWeaknessMultiplier(fireball.getCardType(), knight.getCardType()));
    }
}
