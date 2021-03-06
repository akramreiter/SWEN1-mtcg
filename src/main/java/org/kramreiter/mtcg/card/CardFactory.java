package org.kramreiter.mtcg.card;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CardFactory {
    private final String FILE_COMMON = "src/main/resources/cards_common.csv";
    private final String FILE_RARE = "src/main/resources/cards_rare.csv";
    private final String FILE_EPIC = "src/main/resources/cards_epic.csv";
    private final String FILE_LEGENDARY = "src/main/resources/cards_legendary.csv";

    private static final CardFactory singleton = new CardFactory();

    private CardFactory() {}

    public static CardFactory getInstance() {
        return singleton;
    }

    public Card getCard(String cardId) {
        Card c;
        for (Rarity r : new Rarity[] {Rarity.Common, Rarity.Rare, Rarity.Epic, Rarity.Legendary}) {
            if ((c = getCard(cardId, r)) != null) {
                return c;
            }
        }
        return null;
    }

    public Card[] getFullLibrary() {
        ArrayList<Card> out = new ArrayList<>();
        for (String s : new String[] {
                FILE_COMMON,
                FILE_RARE,
                FILE_EPIC,
                FILE_LEGENDARY
        }) {
            try {
                Card next;
                String[] card;
                CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new FileReader(s));
                while ((card = reader.readNext()) != null) {
                    if (card[3].equals("1")) {
                        next = new CardSpell(
                                card[1],
                                Integer.parseInt(card[4]),
                                Integer.parseInt(card[2]),
                                Integer.parseInt(card[5]),
                                card[7],
                                card[0]
                        );
                    } else {
                        next = new CardMonster(
                                card[1],
                                Integer.parseInt(card[4]),
                                Integer.parseInt(card[2]),
                                Integer.parseInt(card[5]),
                                card[7],
                                Integer.parseInt(card[6]),
                                card[0]
                        );
                    }
                    next.setCustomWin(getCustomWinForId(card[0]));
                    out.add(next);
                }
            } catch (IOException | CsvValidationException e) {
                e.printStackTrace();
            }
        }
        return out.toArray(new Card[0]);
    }

    public Card getCard(String cardId, Rarity rarity) {
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
                                card[7],
                                card[0]
                        );
                    } else {
                        out = new CardMonster(
                                card[1],
                                Integer.parseInt(card[4]),
                                Integer.parseInt(card[2]),
                                Integer.parseInt(card[5]),
                                card[7],
                                Integer.parseInt(card[6]),
                                card[0]
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

    public String getRandomCardIdForRarity(Rarity rarity) {
        String filename = fileFromRarity(rarity);
        try {
            ArrayList<String> options = new ArrayList<>();
            String[] card;
            CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new FileReader(filename));
            while ((card = reader.readNext()) != null) {
                if (card.length > 0 && card[0].trim().length() > 0) {
                    options.add(card[0]);
                }
            }
            return options.get((int)(Math.random() * options.size()));
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String fileFromRarity(Rarity rarity) {
        return switch (rarity) {
            case Common -> FILE_COMMON;
            case Rare -> FILE_RARE;
            case Epic -> FILE_EPIC;
            case Legendary -> FILE_LEGENDARY;
        };
    }

    private String getCustomWinForId(String cardId) {
        return switch (cardId) {
            case "3001" -> "/w spontaneously exploded, taking /l with it";
            case "3002" -> "/l couldn't withstand the /w";
            case "3003" -> "/l was burned to a crisp by /w";
            case "3004" -> "/w swiftly removed /l from the battlefield";
            case "3005" -> "/l was completely crushed by /w";
            case "3006" -> "/w somehow, miraculously won against /l";
            case "3007" -> "/w slammed down on /l with its gargantuan tentacles";
            case "3008" -> "/l was afflicted by /w's curse";
            case "3009" -> "/w used their staff to beat up /l";
            case "3010" -> "/w sneaked up on and defeated /l with a single cut";
            case "3011" -> "/w was unimpressed by /l's pathetic attempts to take them down";
            case "3012" -> "Even past their prime /w could easily defeat /l";
            case "3013" -> "/w's fiery breath roasted /l";
            case "3014" -> "/w took a stand against Goblin oppression and survived against /l";
            default -> null;
        };
    }
}
