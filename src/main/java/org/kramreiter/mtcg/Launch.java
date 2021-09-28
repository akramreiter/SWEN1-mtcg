package org.kramreiter.mtcg;

import org.kramreiter.mtcg.card.*;
import org.kramreiter.mtcg.user.User;

public class Launch {
    public static void main(String[] args) {
        System.out.println("Hello World");

        Card knight = new CardMonster("Basic Boring Human Knight", 50, 0, false, 3, null, 0);
        System.out.println(knight.toString());

        Card fireball = new CardSpell("Fireball", 40, CardType.Fire, true, Rarity.Rare, null);
        System.out.println(fireball.toString());
        System.out.println("effectiveness of " + fireball.getName() + " against " + knight.getName() + ": " + CardManager.computeWeaknessMultiplier(fireball.getCardType(), knight.getCardType()));
    }
}
