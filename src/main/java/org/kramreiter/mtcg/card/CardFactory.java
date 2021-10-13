package org.kramreiter.mtcg.card;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class CardFactory {
    private static final String FILE_COMMON = "src/main/resources/cards_common.csv";
    private static final String FILE_RARE = "src/main/resources/cards_rare.csv";
    private static final String FILE_EPIC = "src/main/resources/cards_epic.csv";
    private static final String FILE_LEGENDARY = "src/main/resources/cards_legendary.csv";

    public static Card getCard(String cardId) {
        Card c;
        for (Rarity r : new Rarity[] {Rarity.Common, Rarity.Rare, Rarity.Epic, Rarity.Legendary}) {
            if ((c = getCard(cardId, r)) != null) {
                return c;
            }
        }
        return null;
    }

    public static Card getCard(String cardId, Rarity rarity) {
        String filename = fileFromRarity(rarity);
        try {
            Card out;
            String[] card;
            CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new FileReader(filename));
            while ((card = reader.readNext()) != null) {
                if (card[0].equals(cardId)) {
                    if (card[3].equals("1")) {
                        out = new CardSpell(
                                card[1],
                                Integer.parseInt(card[4]),
                                Integer.parseInt(card[2]),
                                Integer.parseInt(card[5]),
                                card[7]
                        );
                    } else {
                        out = new CardMonster(
                                card[1],
                                Integer.parseInt(card[4]),
                                Integer.parseInt(card[2]),
                                Integer.parseInt(card[5]),
                                card[7],
                                Integer.parseInt(card[6])
                        );
                    }
                    out.setCustomWin(getCustomWinForId(cardId));
                    return out;
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Card getRandomCardForRarity(Rarity rarity) {
        return null;
    }

    private static String fileFromRarity(Rarity rarity) {
        return switch (rarity) {
            case Common -> FILE_COMMON;
            case Rare -> FILE_RARE;
            case Epic -> FILE_EPIC;
            case Legendary -> FILE_LEGENDARY;
        };
    }

    private static String getCustomWinForId(String cardId) {
        return switch (cardId) {
            case "3001" -> "/w spontaneously exploded, taking /l with it";
            case "3002" -> "/l couldn't withstand the /w";
            case "3003" -> "/l was burned to a crisp by /w";
            case "3004" -> "/w swiftly removed /l from the battlefield";
            case "3005" -> "A mass-/w event befell /l";
            case "3006" -> "/w somehow, miraculously won against /l";
            case "3007" -> "/w slammed down on /l with its gargantuan tentacles";
            case "3008" -> "/l was afflicted by /w's curse";
            case "3009" -> "/w used their staff to beat up /l";
            case "3010" -> "/w sneaked up on and defeated /l with a single cut";
            case "3011" -> "/w was unimpressed by /l's pathetic attempts to take them down";
            case "3012" -> "/w may not be at the height of their strength, but even so, /l stood no chance against them";
            case "3013" -> "/w's fiery breath roasted /l";
            case "3014" -> "/w took a stand against Goblin oppression and survived the encounter against /l";
            default -> null;
        };
    }
}
