package com.juanluis.cupheadclicker.ui.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Data Access Object with all the SQL operations of the "players" table.
 * <p>
 * The methods are synchronous, so they must be called from a background
 * thread (Room throws an exception if they are called on the main thread).
 * </p>
 *
 * @author Juan Luis Simancas Prundea
 * @version 1.0
 */
@Dao
public interface PlayerDao {

    /** Inserts a player only if the username does not exist yet. */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertIfAbsent(PlayerEntity player);

    /** Inserts or replaces a player (used to save progress). */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsert(PlayerEntity player);

    /** @return the player with that username, or {@code null} */
    @Query("SELECT * FROM players WHERE username = :username LIMIT 1")
    PlayerEntity findByUsername(String username);

    /** @return all players, best score first */
    @Query("SELECT * FROM players ORDER BY score DESC")
    List<PlayerEntity> getAllByScore();

    /** @return all players, highest level first */
    @Query("SELECT * FROM players ORDER BY level DESC, score DESC")
    List<PlayerEntity> getAllByLevel();

    /** @return all players in alphabetical order */
    @Query("SELECT * FROM players ORDER BY username COLLATE NOCASE ASC")
    List<PlayerEntity> getAllByName();

    /** @return number of players saved */
    @Query("SELECT COUNT(*) FROM players")
    int count();
}
