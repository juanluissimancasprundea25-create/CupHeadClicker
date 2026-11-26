package com.juanluis.cupheadclicker.ui.data.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.juanluis.cupheadclicker.ui.data.db.AppDatabase;
import com.juanluis.cupheadclicker.ui.data.db.PlayerDao;
import com.juanluis.cupheadclicker.ui.data.db.PlayerEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Single access point to the player data (Repository pattern).
 * <p>
 * Every database operation runs in a background thread of an
 * {@link ExecutorService}, so the main (UI) thread is never blocked. The
 * results are sent back to the main thread with a {@link Handler} linked to
 * the main {@link Looper}.
 * </p>
 * <p>
 * A single-thread executor is used on purpose: the operations run in the
 * same order they are requested (for example, "save" always finishes before
 * a later "load ranking").
 * </p>
 *
 * @author Juan Luis Simancas Prundea
 * @version 1.0
 */
public class PlayerRepository {

    /** Order options of the ranking screen (same order as the spinner). */
    public enum SortOrder { SCORE, LEVEL, NAME }

    /**
     * Callback that receives the result of an operation on the main thread.
     *
     * @param <T> type of the result
     */
    public interface Callback<T> {
        void onResult(T result);
    }

    private static final String[] RIVAL_NAMES = {
            "Lola_Manivela", "Bobby_Bombin", "Paco_Pianola", "Rita_Ragtime", "Tito_Trombon"
    };

    private static volatile PlayerRepository instance;

    private final PlayerDao playerDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private PlayerRepository(Context context) {
        playerDao = AppDatabase.getInstance(context).playerDao();
        executor.execute(this::seedRivalsIfEmpty);
    }

    /**
     * @param context any context; only its application context is kept
     * @return the only instance of the repository
     */
    public static PlayerRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (PlayerRepository.class) {
                if (instance == null) {
                    instance = new PlayerRepository(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    /**
     * Creates the player if it is new and returns their saved data.
     *
     * @param username name of the player
     * @param callback receives the player on the main thread
     */
    public void loginPlayer(String username, Callback<PlayerEntity> callback) {
        executor.execute(() -> {
            playerDao.insertIfAbsent(new PlayerEntity(username));
            deliver(callback, playerDao.findByUsername(username));
        });
    }

    /**
     * Loads the progress of a player.
     *
     * @param username name of the player
     * @param callback receives the player (or {@code null}) on the main thread
     */
    public void loadPlayer(String username, Callback<PlayerEntity> callback) {
        executor.execute(() -> deliver(callback, playerDao.findByUsername(username)));
    }

    /**
     * Saves the progress of a player in the background.
     *
     * @param player data to save
     */
    public void savePlayer(PlayerEntity player) {
        executor.execute(() -> playerDao.upsert(player));
    }

    /**
     * Loads the ranking with the selected order.
     *
     * @param order    order criteria
     * @param callback receives the list on the main thread
     */
    public void loadRanking(SortOrder order, Callback<List<PlayerEntity>> callback) {
        executor.execute(() -> {
            List<PlayerEntity> players;
            switch (order) {
                case LEVEL:
                    players = playerDao.getAllByLevel();
                    break;
                case NAME:
                    players = playerDao.getAllByName();
                    break;
                case SCORE:
                default:
                    players = playerDao.getAllByScore();
                    break;
            }
            deliver(callback, players);
        });
    }

    /** Adds some rival players the first time, so the ranking is not empty. */
    private void seedRivalsIfEmpty() {
        if (playerDao.count() > 0) {
            return;
        }
        for (int i = 0; i < RIVAL_NAMES.length; i++) {
            PlayerEntity rival = new PlayerEntity(RIVAL_NAMES[i]);
            rival.level = i + 1;
            rival.score = (i + 1) * 750L;
            playerDao.insertIfAbsent(rival);
        }
    }

    /** Sends a result to the main thread. */
    private <T> void deliver(Callback<T> callback, T result) {
        mainHandler.post(() -> callback.onResult(result));
    }
}
