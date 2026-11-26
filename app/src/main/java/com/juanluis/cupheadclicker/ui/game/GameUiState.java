package com.juanluis.cupheadclicker.ui.game;

import com.juanluis.cupheadclicker.ui.domain.Boss;
import com.juanluis.cupheadclicker.ui.domain.GameEngine;

/**
 * Immutable "photo" of everything the game screen has to paint.
 * The Activity only reads these values; it never touches the engine.
 *
 * @author Juan Luis Simancas Prundea
 * @version 1.0
 */
public final class GameUiState {

    public final String bossName;
    public final int bossColor;
    public final long bossHp;
    public final long bossMaxHp;
    public final int level;
    public final String rankTitle;
    public final long coins;
    public final long score;
    public final long tapDamage;
    public final long autoDamage;
    public final long tapUpgradeCost;
    public final long autoUpgradeCost;
    public final boolean canBuyTapUpgrade;
    public final boolean canBuyAutoUpgrade;

    private GameUiState(GameEngine engine) {
        Boss boss = engine.getCurrentBoss();
        bossName = boss.getName();
        bossColor = boss.getColor();
        bossHp = engine.getBossHp();
        bossMaxHp = boss.getMaxHp();
        level = engine.getLevel();
        rankTitle = engine.getRank().getTitle();
        coins = engine.getCoins();
        score = engine.getScore();
        tapDamage = engine.getTapDamage();
        autoDamage = engine.getAutoDamage();
        tapUpgradeCost = engine.getTapUpgradeCost();
        autoUpgradeCost = engine.getAutoUpgradeCost();
        canBuyTapUpgrade = coins >= tapUpgradeCost;
        canBuyAutoUpgrade = coins >= autoUpgradeCost;
    }

    /**
     * @param engine current game
     * @return a new state with the values of the engine
     */
    public static GameUiState from(GameEngine engine) {
        return new GameUiState(engine);
    }

    /** @return health left in percent (0-100) for the progress bar */
    public int getHpPercent() {
        return bossMaxHp == 0 ? 0 : (int) (bossHp * 100 / bossMaxHp);
    }
}
