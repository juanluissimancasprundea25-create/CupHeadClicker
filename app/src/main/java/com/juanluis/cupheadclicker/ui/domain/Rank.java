package com.juanluis.cupheadclicker.ui.domain;

/**
 * Title of the player in the character sheet. It evolves with the number of
 * bosses defeated.
 *
 * @author Juan Luis Simancas Prundea
 * @version 1.0
 */
public enum Rank {
    ROOKIE("Novato de feria", 0),
    DECKHAND("Grumete", 1),
    ADVENTURER("Aventurero", 2),
    VETERAN("Veterano", 3),
    CHAMPION("Campeón", 4),
    LEGEND("Leyenda en blanco y negro", 5);

    private final String title;
    private final int minBossesDefeated;

    Rank(String title, int minBossesDefeated) {
        this.title = title;
        this.minBossesDefeated = minBossesDefeated;
    }

    /** @return text shown on screen */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the best rank reached with a number of defeated bosses.
     *
     * @param bossesDefeated number of bosses defeated
     * @return the matching rank
     */
    public static Rank fromBossesDefeated(int bossesDefeated) {
        Rank result = ROOKIE;
        for (Rank rank : values()) {
            if (bossesDefeated >= rank.minBossesDefeated) {
                result = rank;
            }
        }
        return result;
    }
}
