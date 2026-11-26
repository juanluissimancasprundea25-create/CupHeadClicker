package com.juanluis.cupheadclicker.ui.domain;

/**
 * Core rules of the tap game: damage, coins, score, levels and upgrades.
 * <p>
 * It is plain Java (no Android classes), so all the rules can be tested with
 * unit tests. The ViewModel owns one instance and calls it from the main
 * thread only, so no synchronization is needed.
 * </p>
 * <ul>
 *     <li>Every damage point gives 1 coin and 1 score point.</li>
 *     <li>Defeating a boss gives its reward in coins and reward x10 in score.</li>
 *     <li>Tap upgrade: +1 damage per tap. Auto upgrade: +1 damage per second.</li>
 * </ul>
 *
 * @author Juan Luis Simancas Prundea
 * @version 1.0
 */
public class GameEngine {

    public static final long TAP_UPGRADE_BASE_COST = 15;
    public static final double TAP_UPGRADE_GROWTH = 1.5;
    public static final long AUTO_UPGRADE_BASE_COST = 50;
    public static final double AUTO_UPGRADE_GROWTH = 1.6;
    public static final int SCORE_PER_REWARD_COIN = 10;

    private int level;
    private long bossHp;
    private long coins;
    private long score;
    private int tapLevel;
    private int autoLevel;

    /**
     * Restores a game from saved values. Wrong values are corrected.
     *
     * @param level     current level (min 1)
     * @param bossHp    health left of the current boss (0 or invalid = full)
     * @param coins     coins of the player
     * @param score     total score
     * @param tapLevel  level of the tap upgrade
     * @param autoLevel level of the auto damage upgrade
     */
    public GameEngine(int level, long bossHp, long coins, long score, int tapLevel, int autoLevel) {
        this.level = Math.max(1, level);
        long maxHp = getCurrentBoss().getMaxHp();
        this.bossHp = (bossHp <= 0 || bossHp > maxHp) ? maxHp : bossHp;
        this.coins = Math.max(0, coins);
        this.score = Math.max(0, score);
        this.tapLevel = Math.max(0, tapLevel);
        this.autoLevel = Math.max(0, autoLevel);
    }

    /** @return a new game at level 1 */
    public static GameEngine newGame() {
        return new GameEngine(1, 0, 0, 0, 0, 0);
    }

    /**
     * Applies the damage of one tap.
     *
     * @return {@code true} if the boss was defeated with this tap
     */
    public boolean tap() {
        return applyDamage(getTapDamage());
    }

    /**
     * Applies the automatic damage of one second.
     *
     * @return {@code true} if the boss was defeated
     */
    public boolean tick() {
        long damage = getAutoDamage();
        return damage > 0 && applyDamage(damage);
    }

    /**
     * Subtracts health from the boss and gives coins and score.
     *
     * @param damage damage to apply
     * @return {@code true} if the boss was defeated
     */
    private boolean applyDamage(long damage) {
        long dealt = Math.min(damage, bossHp);
        bossHp -= dealt;
        coins += dealt;
        score += dealt;
        if (bossHp == 0) {
            Boss defeated = getCurrentBoss();
            coins += defeated.getReward();
            score += defeated.getReward() * SCORE_PER_REWARD_COIN;
            level++;
            bossHp = getCurrentBoss().getMaxHp();
            return true;
        }
        return false;
    }

    /**
     * Buys the tap upgrade if the player has enough coins.
     *
     * @return {@code true} if the upgrade was bought
     */
    public boolean buyTapUpgrade() {
        long cost = getTapUpgradeCost();
        if (coins < cost) {
            return false;
        }
        coins -= cost;
        tapLevel++;
        return true;
    }

    /**
     * Buys the auto damage upgrade if the player has enough coins.
     *
     * @return {@code true} if the upgrade was bought
     */
    public boolean buyAutoUpgrade() {
        long cost = getAutoUpgradeCost();
        if (coins < cost) {
            return false;
        }
        coins -= cost;
        autoLevel++;
        return true;
    }

    /** @return damage of one tap */
    public long getTapDamage() {
        return 1L + tapLevel;
    }

    /** @return automatic damage per second */
    public long getAutoDamage() {
        return autoLevel;
    }

    /** @return price of the next tap upgrade (it grows exponentially) */
    public long getTapUpgradeCost() {
        return Math.round(TAP_UPGRADE_BASE_COST * Math.pow(TAP_UPGRADE_GROWTH, tapLevel));
    }

    /** @return price of the next auto upgrade (it grows exponentially) */
    public long getAutoUpgradeCost() {
        return Math.round(AUTO_UPGRADE_BASE_COST * Math.pow(AUTO_UPGRADE_GROWTH, autoLevel));
    }

    /** @return boss of the current level */
    public Boss getCurrentBoss() {
        return BossCatalog.forLevel(level);
    }

    /** @return number of bosses defeated so far */
    public int getBossesDefeated() {
        return level - 1;
    }

    /** @return title of the character sheet */
    public Rank getRank() {
        return Rank.fromBossesDefeated(getBossesDefeated());
    }

    public int getLevel() {
        return level;
    }

    public long getBossHp() {
        return bossHp;
    }

    public long getCoins() {
        return coins;
    }

    public long getScore() {
        return score;
    }

    public int getTapLevel() {
        return tapLevel;
    }

    public int getAutoLevel() {
        return autoLevel;
    }
}
