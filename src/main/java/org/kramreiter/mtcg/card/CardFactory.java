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
        return null;
    }

    public static Card getCard(String cardId, Rarity rarity) {
        String filename = fileFromRarity(rarity);
        try {
            String[] card;
            CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new FileReader(filename));
            while ((card = reader.readNext()) != null) {
                if (card[0].equals(cardId)) {
                    if (card[3].equals("1")) {
                        return new CardSpell(
                                card[1],
                                Integer.parseInt(card[4]),
                                Integer.parseInt(card[2]),
                                Integer.parseInt(card[5]),
                                card[7]
                        );
                    } else {
                        return new CardMonster(
                                card[1],
                                Integer.parseInt(card[4]),
                                Integer.parseInt(card[2]),
                                Integer.parseInt(card[5]),
                                card[7],
                                Integer.parseInt(card[6])
                        );
                    }
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
}
