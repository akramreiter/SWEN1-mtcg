package org.kramreiter.mtcg.card;
import org.kramreiter.mtcg.user.User;

import java.util.ArrayList;

public class GameManager {
    private static final int MAXTURNS = 100;
    private static final String DRAWMSG = "No winner could be decided between /w and /l";

    /**
     * Returns the whole history of a game between two players
     * @param player1
     * @param player2
     * @return
     */
    public static String[] runGame(User player1, User player2, GameMode mode) {
        Deck player1Deck = new Deck(player1.getDeckCards(), player1.getUsername());
        Deck player2Deck = new Deck(player2.getDeckCards(), player2.getUsername());
        ArrayList<String> outcomes = new ArrayList<>();
        outcomes.add("Ranked game between " + player1.getUsername() + " and " + player2.getUsername());
        outcomes.add("Playing in " + mode + " mode");

        for (Card c : player1Deck.getCards()) {
            execEffect(player1Deck, player2Deck, outcomes, c, null, EffectTime.GameStart, null);
        }
        for (Card c : player2Deck.getCards()) {
            execEffect(player2Deck, player1Deck, outcomes, c, null, EffectTime.GameStart, null);
        }

        int turnCount = 0;
        while (player1Deck.getCards().length > 0 && player2Deck.getCards().length > 0 && turnCount < MAXTURNS) {
            turnCount++;
            outcomes.add("[TURN " + turnCount + "] - " + player1.getUsername() + ": " + player1Deck.getCards().length + "; "  + player2.getUsername() + ": " + player2Deck.getCards().length);
            CombatOutcome turn = calcTurn(player1Deck, player2Deck, outcomes);
            switch (mode) {
                case Classic -> {
                    Card swapCard = turn.getLoser();
                    if (player1Deck.removeCard(swapCard)) {
                        swapCard.setOwnerName(player2Deck.getPlayerName());
                        player2Deck.addCard(swapCard);
                    } else {
                        player2Deck.removeCard(swapCard);
                        swapCard.setOwnerName(player1Deck.getPlayerName());
                        player1Deck.addCard(swapCard);
                    }
                }
                case Structured -> {
                    player1Deck.removeCard(turn.getLoser());
                    player2Deck.removeCard(turn.getLoser());
                }
            }
        }

        if (player1Deck.getCards().length > player2Deck.getCards().length) {
            eloAdjust(player1, player2, outcomes, player1Deck, player2Deck);
        } else if (player2Deck.getCards().length > player1Deck.getCards().length) {
            eloAdjust(player2, player1, outcomes, player2Deck, player1Deck);
        } else {
            outcomes.add("The game between " + player1.getUsername() + " and " + player2.getUsername() + " ended in a draw");
        }
        return outcomes.toArray(new String[0]);
    }

    private static void eloAdjust(User winner, User loser, ArrayList<String> outcomes, Deck winnerDeck, Deck loserDeck) {
        outcomes.add(winner.getUsername() + " won against " + loser.getUsername() + " [" + winnerDeck.getCards().length + ":" + loserDeck.getCards().length + "]");
        int eloChange = winner.computeEloGainForWin(loser);
        winner.adjustElo(eloChange);
        loser.adjustElo(-eloChange);
        outcomes.add(winner.getUsername() + ": " + winner.getElo() + "[+" + eloChange + "]; " + loser.getUsername() + ": " +
                loser.getElo() + "[-" + eloChange + "]");
    }

    /**
     * calculates the CombatOutcome for a single turn
     * called by runGame()
     * @param p1Deck
     * @param p2Deck
     * @return
     */
    public static CombatOutcome calcTurn(Deck p1Deck, Deck p2Deck, ArrayList<String> outcomes) {
        Card p1Card = p1Deck.getCards()[(int) (p1Deck.getCards().length * Math.random())];
        Card p2Card = p2Deck.getCards()[(int) (p2Deck.getCards().length * Math.random())];

        CombatOutcome res = null;

        execEffect(p1Deck, p2Deck, outcomes, p1Card, p2Card, EffectTime.BeforeCombat, null);
        execEffect(p2Deck, p1Deck, outcomes, p2Card, p1Card, EffectTime.BeforeCombat, null);
        for (SpecialRule rule : RuleManager.getRuleset()) {
            if ((res = rule.executeRule(p1Card, p2Card)) != null || (res = rule.executeRule(p2Card, p1Card)) != null) {
                break;
            }
        }
        SpecialRule[] rules = RuleManager.getRuleset();
        for (SpecialRule rule : rules) {
            if ((res = rule.executeRule(p1Card, p2Card)) != null) break;
            if ((res = rule.executeRule(p2Card, p1Card)) != null) break;
        }
        if (res == null) {
            Card[] outcome = selectWinner(p1Card, p2Card);
            if (outcome != null) {
                res = new CombatOutcome(null, outcome[0], outcome[1]);
            } else {
                res = new CombatOutcome(DRAWMSG, p1Card, p2Card, true);
            }
        }
        outcomes.add(res.getMessage());

        execEffect(p1Deck, p2Deck, outcomes, p1Card, p2Card, EffectTime.AfterCombat, res);
        execEffect(p2Deck, p1Deck, outcomes, p2Card, p1Card, EffectTime.AfterCombat, res);
        return res;
    }

    private static void execEffect(Deck selfDeck, Deck opponentDeck, ArrayList<String> outcomes, Card self, Card opponent, EffectTime time, CombatOutcome prevOutcome) {
        if (self.getEffect() != null && self.getEffect().getEffectTime() == time) {
            String effOutcome = self.getEffect().executeEffect(
                    self,
                    opponent,
                    selfDeck,
                    opponentDeck,
                    prevOutcome
            );
            if (effOutcome != null) {
                outcomes.add(self.getName() + " (" + self.getOwnerName() + "): " + effOutcome);
            }
        }
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
