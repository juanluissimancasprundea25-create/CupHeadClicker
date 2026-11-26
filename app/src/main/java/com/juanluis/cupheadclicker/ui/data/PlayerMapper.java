package com.juanluis.cupheadclicker.ui.data;

import com.juanluis.cupheadclicker.ui.data.db.PlayerEntity;
import com.juanluis.cupheadclicker.ui.domain.GameEngine;

/**
 * Converts between the database entity and the game engine, so the domain
 * layer does not depend on Room.
 *
 * @author Juan Luis Simancas Prundea
 * @version 1.0
 */
public final class PlayerMapper {

    /** Utility class: it must not be instantiated. */
    private PlayerMapper() {
    }

    /**
     * @param player saved player
     * @return a game engine with the saved progress
     */
    public static GameEngine toEngine(PlayerEntity player) {
        return new GameEngine(player.level, player.bossHp, player.coins,
                player.score, player.tapLevel, player.autoLevel);
    }

    /**
     * @param username name of the player
     * @param engine   current game
     * @return an entity ready to be saved
     */
    public static PlayerEntity toEntity(String username, GameEngine engine) {
        PlayerEntity player = new PlayerEntity(username);
        player.level = engine.getLevel();
        player.bossHp = engine.getBossHp();
        player.coins = engine.getCoins();
        player.score = engine.getScore();
        player.tapLevel = engine.getTapLevel();
        player.autoLevel = engine.getAutoLevel();
        player.lastPlayed = System.currentTimeMillis();
        return player;
    }
}
