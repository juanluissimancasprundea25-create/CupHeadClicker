package com.juanluis.cupheadclicker.ui.data.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Room entity that stores one player and the progress of their character.
 * Each row of the "players" table is one player of the ranking.
 *
 * @author Juan Luis Simancas Prundea
 * @version 1.0
 */
@Entity(tableName = "players")
public class PlayerEntity {

    /** Username, used as primary key (it cannot be repeated). */
    @PrimaryKey
    @NonNull
    public String username;

    /** Current level (boss number). */
    public int level;

    /** Health left of the current boss. */
    public long bossHp;

    /** Coins to buy upgrades. */
    public long coins;

    /** Total score, used in the ranking. */
    public long score;

    /** Level of the tap damage upgrade. */
    public int tapLevel;

    /** Level of the automatic damage upgrade. */
    public int autoLevel;

    /** Last time the player saved, in milliseconds. */
    public long lastPlayed;

    /**
     * Creates a new player at level 1.
     *
     * @param username name of the player
     */
    public PlayerEntity(@NonNull String username) {
        this.username = username;
        this.level = 1;
    }
}
