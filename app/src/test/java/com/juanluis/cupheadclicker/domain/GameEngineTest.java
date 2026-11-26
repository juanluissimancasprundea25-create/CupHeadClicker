package com.juanluis.cupheadclicker.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.juanluis.cupheadclicker.ui.domain.Boss;
import com.juanluis.cupheadclicker.ui.domain.BossCatalog;
import com.juanluis.cupheadclicker.ui.domain.GameEngine;
import com.juanluis.cupheadclicker.ui.domain.Rank;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link GameEngine}, {@link BossCatalog} and {@link Rank}.
 *
 * @author Juan Luis Simancas Prundea
 * @version 1.0
 */
public class GameEngineTest {

    private GameEngine engine;

    @Before
    public void setUp() {
        engine = GameEngine.newGame();
    }

    @Test
    public void newGame_startsAtLevelOneWithFullHp() {
        assertEquals(1, engine.getLevel());
        assertEquals(BossCatalog.forLevel(1).getMaxHp(), engine.getBossHp());
        assertEquals(0, engine.getCoins());
    }

    @Test
    public void tap_reducesHpAndGivesCoins() {
        long hpBefore = engine.getBossHp();
        engine.tap();
        assertEquals(hpBefore - 1, engine.getBossHp());
        assertEquals(1, engine.getCoins());
        assertEquals(1, engine.getScore());
    }

    @Test
    public void defeatingBoss_goesToNextLevelAndGivesReward() {
        Boss first = engine.getCurrentBoss();
        boolean defeated = false;
        for (int i = 0; i < first.getMaxHp(); i++) {
            defeated = engine.tap();
        }
        assertTrue(defeated);
        assertEquals(2, engine.getLevel());
        assertEquals(first.getMaxHp() + first.getReward(), engine.getCoins());
        assertEquals(BossCatalog.forLevel(2).getMaxHp(), engine.getBossHp());
    }

    @Test
    public void buyTapUpgrade_withoutCoins_fails() {
        assertFalse(engine.buyTapUpgrade());
        assertEquals(1, engine.getTapDamage());
    }

    @Test
    public void buyTapUpgrade_withCoins_increasesDamage() {
        GameEngine rich = new GameEngine(1, 0, 100, 0, 0, 0);
        assertTrue(rich.buyTapUpgrade());
        assertEquals(2, rich.getTapDamage());
        assertEquals(100 - GameEngine.TAP_UPGRADE_BASE_COST, rich.getCoins());
    }

    @Test
    public void tick_withoutAutoUpgrade_doesNothing() {
        long hpBefore = engine.getBossHp();
        assertFalse(engine.tick());
        assertEquals(hpBefore, engine.getBossHp());
    }

    @Test
    public void tick_withAutoUpgrade_damagesBoss() {
        GameEngine auto = new GameEngine(1, 0, 0, 0, 0, 3);
        long hpBefore = auto.getBossHp();
        auto.tick();
        assertEquals(hpBefore - 3, auto.getBossHp());
    }

    @Test
    public void upgradeCost_growsAfterEachPurchase() {
        GameEngine rich = new GameEngine(1, 0, 1000, 0, 0, 0);
        long firstCost = rich.getTapUpgradeCost();
        rich.buyTapUpgrade();
        assertTrue(rich.getTapUpgradeCost() > firstCost);
    }

    @Test
    public void invalidSavedValues_areCorrected() {
        GameEngine broken = new GameEngine(-3, 999999, -5, -1, -2, -2);
        assertEquals(1, broken.getLevel());
        assertEquals(BossCatalog.forLevel(1).getMaxHp(), broken.getBossHp());
        assertEquals(0, broken.getCoins());
        assertEquals(0, broken.getTapLevel());
    }

    @Test
    public void bossCatalog_secondLapDoublesHp() {
        Boss firstLap = BossCatalog.forLevel(1);
        Boss secondLap = BossCatalog.forLevel(1 + BossCatalog.BOSS_COUNT);
        assertEquals(firstLap.getMaxHp() * 2, secondLap.getMaxHp());
    }

    @Test
    public void rank_evolvesWithBossesDefeated() {
        assertEquals(Rank.ROOKIE, Rank.fromBossesDefeated(0));
        assertEquals(Rank.VETERAN, Rank.fromBossesDefeated(3));
        assertEquals(Rank.LEGEND, Rank.fromBossesDefeated(12));
    }
}
