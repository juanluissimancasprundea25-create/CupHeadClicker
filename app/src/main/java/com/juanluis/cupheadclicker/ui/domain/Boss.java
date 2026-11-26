package com.juanluis.cupheadclicker.ui.domain;

/**
 * Immutable model of a boss the player has to defeat by tapping.
 *
 * @author Juan Luis Simancas Prundea
 * @version 1.0
 */
public final class Boss {

    private final String name;
    private final long maxHp;
    private final long reward;
    private final int color;

    /**
     * Creates a boss.
     *
     * @param name   name shown on screen
     * @param maxHp  total health points
     * @param reward coins given when the boss is defeated
     * @param color  ARGB color used to paint the boss body
     */
    public Boss(String name, long maxHp, long reward, int color) {
        this.name = name;
        this.maxHp = maxHp;
        this.reward = reward;
        this.color = color;
    }

    /** @return name of the boss */
    public String getName() {
        return name;
    }

    /** @return total health points */
    public long getMaxHp() {
        return maxHp;
    }

    /** @return coins given when the boss is defeated */
    public long getReward() {
        return reward;
    }

    /** @return ARGB color of the boss body */
    public int getColor() {
        return color;
    }
}
