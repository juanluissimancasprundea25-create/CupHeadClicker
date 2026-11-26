package com.juanluis.cupheadclicker.ui.domain;

/**
 * Catalog with the five bosses of the game.
 * <p>
 * When the player beats the fifth boss the list starts again ("lap"), and
 * every lap doubles the health and the reward of the bosses.
 * </p>
 *
 * @author Juan Luis Simancas Prundea
 * @version 1.0
 */
public final class BossCatalog {

    /** Number of different bosses. */
    public static final int BOSS_COUNT = 5;

    /** Max lap used for the multiplier, to avoid a long overflow. */
    private static final int MAX_LAP = 20;

    private static final Boss[] BOSSES = {
            new Boss("Barón Tetera", 50, 10, 0xFFC8102E),
            new Boss("Madame Nubarrón", 150, 30, 0xFF6B8FB3),
            new Boss("Rey Gramófono", 400, 80, 0xFFE0A526),
            new Boss("Capitán Bobina", 1000, 200, 0xFF4F7942),
            new Boss("El Proyeccionista", 2500, 500, 0xFF5B3A6E)
    };

    /** Utility class: it must not be instantiated. */
    private BossCatalog() {
    }

    /**
     * Returns the boss of a level.
     *
     * @param level level of the player, starting at 1
     * @return boss of that level, stronger in every lap
     * @throws IllegalArgumentException if the level is lower than 1
     */
    public static Boss forLevel(int level) {
        if (level < 1) {
            throw new IllegalArgumentException("Level must be 1 or higher");
        }
        int index = (level - 1) % BOSS_COUNT;
        int lap = (level - 1) / BOSS_COUNT;
        Boss base = BOSSES[index];
        if (lap == 0) {
            return base;
        }
        long multiplier = 1L << Math.min(lap, MAX_LAP);
        return new Boss(base.getName() + " +" + lap,
                base.getMaxHp() * multiplier,
                base.getReward() * multiplier,
                base.getColor());
    }
}
