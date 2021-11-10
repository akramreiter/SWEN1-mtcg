package org.kramreiter.mtcg;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kramreiter.mtcg.card.Card;
import org.kramreiter.mtcg.card.CardMonster;
import org.kramreiter.mtcg.card.CardSpell;

public class CardTests {
    Card testSpell;
    Card testMonster;

    @BeforeEach
    void setupCards() {
        testSpell = new CardSpell("hi",50,2,0,null);
        testMonster = new CardMonster("hi2", 70, 0, 0, null, 0);
        System.out.println(testMonster);
        System.out.println(testSpell);
    }

    @Test
    void cardSpellIsSpell() {
        assert testSpell.isSpell();
    }

    @Test
    void cardMonsterNotSpell() {
        assert !testMonster.isSpell();
    }

    @Test
    void cardTestEffectivity() {
        assert testSpell.getPower() < testMonster.getPower();
        System.out.println(testMonster.computeStrengthAgainst(testSpell) + " < " + testSpell.computeStrengthAgainst(testMonster));
        assert testMonster.computeStrengthAgainst(testSpell) < testSpell.computeStrengthAgainst(testMonster);
    }
}
