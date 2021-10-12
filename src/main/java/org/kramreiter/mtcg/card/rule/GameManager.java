package org.kramreiter.mtcg.card.rule;

import org.kramreiter.mtcg.card.*;
import org.kramreiter.mtcg.user.User;

import java.util.Scanner;

public class GameManager {
    private static final int MAXTURNS = 100;
    private static final String DRAWMSG = "No winner could be decided between /w and /l";

    /**
     * Returns the whole history of a game between two players
     * @param player1
     * @param player2
     * @return
     */
    public static String runGame(User player1, User player2) {
        Deck player1Deck = new Deck(player1.getDeckCards(), player1.getUsername());
        Deck player2Deck = new Deck(player2.getDeckCards(), player2.getUsername());
        StringBuilder outcomes = new StringBuilder();
        outcomes.append("Ranked game between ").append(player1.getUsername()).append(" and ").append(player2.getUsername()).append("\n");

        int turnCount = 0;
        while (player1Deck.getCards().length > 0 && player2Deck.getCards().length > 0 && turnCount < MAXTURNS) {
            turnCount++;
            CombatOutcome turn = calcTurn(player1Deck, player2Deck);
        }
        return "";
    }

    /**
     * calculates the CombatOutcome for a single turn
     * called by runGame()
     * @param p1Deck
     * @param p2Deck
     * @return
     */
    public static CombatOutcome calcTurn(Deck p1Deck, Deck p2Deck) {
        Card p1Card = p1Deck.getCards()[(int) (p1Deck.getCards().length * Math.random())];
        Card p2Card = p2Deck.getCards()[(int) (p2Deck.getCards().length * Math.random())];

        CombatOutcome res = null;

        if (p1Card.getEffect() != null && p1Card.getEffect().getEffectTime() == EffectTime.BeforeCombat) {
            p1Card.getEffect().executeEffect(
                    p1Card,
                    p2Card,
                    p1Deck,
                    p2Deck,
                    null
            );
        }
        if (p2Card.getEffect() != null && p2Card.getEffect().getEffectTime() == EffectTime.BeforeCombat) {
            p2Card.getEffect().executeEffect(
                    p2Card,
                    p1Card,
                    p2Deck,
                    p1Deck,
                    null
            );
        }
        for (SpecialRule rule : RuleManager.getRuleset()) {
            if ((res = rule.executeRule(p1Card, p2Card)) != null || (res = rule.executeRule(p2Card, p1Card)) != null) {
                break;
            }
        }
        if (res == null) {
            Card[] outcome = selectWinner(p1Card, p2Card);
            if (outcome != null) {
                res = new CombatOutcome(null, outcome[0], outcome[1]);
            } else {
                res = new CombatOutcome(DRAWMSG, p1Card, p2Card, true);
            }
        }

        if (p1Card.getEffect() != null && p1Card.getEffect().getEffectTime() == EffectTime.AfterCombat) {
            p1Card.getEffect().executeEffect(
                    p1Card,
                    p2Card,
                    p1Deck,
                    p2Deck,
                    res
            );
        }
        if (p2Card.getEffect() != null && p2Card.getEffect().getEffectTime() == EffectTime.BeforeCombat) {
            p2Card.getEffect().executeEffect(
                    p2Card,
                    p1Card,
                    p2Deck,
                    p1Deck,
                    null
            );
        }
        return res;
    }

    public static Card[] selectWinner(Card p1, Card p2) {
        int p1Str = p1.computeStrengthAgainst(p2);
        int p2Str = p2.computeStrengthAgainst(p1);
        if (p1Str > p2Str) {
            return new Card[] {
                    p1,
                    p2
            };
        } else if (p2Str > p1Str) {
            return new Card[] {
                    p2,
                    p1
            };
        }
        return null;
    }
}
