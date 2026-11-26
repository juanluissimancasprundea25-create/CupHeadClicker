package com.juanluis.cupheadclicker.ui.game;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.juanluis.cupheadclicker.ui.data.PlayerMapper;
import com.juanluis.cupheadclicker.ui.data.prefs.SessionManager;
import com.juanluis.cupheadclicker.ui.data.repository.PlayerRepository;
import com.juanluis.cupheadclicker.ui.domain.GameEngine;

/**
 * Logic of the game screen. It owns the {@link GameEngine}, runs the
 * automatic damage every second and saves the progress.
 * <p>
 * The engine is only used from the main thread (taps and the timer
 * {@link Handler} both run there), so there are no race conditions.
 * The timer is removed in {@link #onCleared()} to avoid memory leaks.
 * </p>
 *
 * @author Juan Luis Simancas Prundea
 * @version 1.0
 */
public class GameViewModel extends AndroidViewModel {

    private static final long TICK_INTERVAL_MS = 1000L;

    private final PlayerRepository repository;
    private final String username;
    private final Handler tickHandler = new Handler(Looper.getMainLooper());

    private final MutableLiveData<GameUiState> uiState = new MutableLiveData<>();
    private final MutableLiveData<String> bossDefeatedEvent = new MutableLiveData<>();

    private GameEngine engine;
    private boolean screenVisible;

    /** Runs the automatic damage and schedules itself again. */
    private final Runnable tickRunnable = new Runnable() {
        @Override
        public void run() {
            if (engine != null) {
                String bossName = engine.getCurrentBoss().getName();
                onDamageApplied(engine.tick(), bossName);
            }
            tickHandler.postDelayed(this, TICK_INTERVAL_MS);
        }
    };

    /**
     * @param application application context
     */
    public GameViewModel(@NonNull Application application) {
        super(application);
        repository = PlayerRepository.getInstance(application);
        username = new SessionManager(application).getUsername();
        loadGame();
    }

    /** Loads the saved progress in a background thread. */
    private void loadGame() {
        if (username == null) {
            startEngine(GameEngine.newGame());
            return;
        }
        repository.loadPlayer(username, player ->
                startEngine(player == null ? GameEngine.newGame() : PlayerMapper.toEngine(player)));
    }

    /** Called on the main thread when the game is ready. */
    private void startEngine(GameEngine loadedEngine) {
        engine = loadedEngine;
        publishState();
        if (screenVisible) {
            startTicker();
        }
    }

    /** The player tapped the boss. */
    public void onBossTapped() {
        if (engine == null) {
            return;
        }
        String bossName = engine.getCurrentBoss().getName();
        onDamageApplied(engine.tap(), bossName);
    }

    /** The player wants to buy the tap upgrade. */
    public void onBuyTapUpgrade() {
        if (engine != null && engine.buyTapUpgrade()) {
            publishState();
        }
    }

    /** The player wants to buy the automatic damage upgrade. */
    public void onBuyAutoUpgrade() {
        if (engine != null && engine.buyAutoUpgrade()) {
            publishState();
        }
    }

    /** The screen is visible: start the automatic damage. */
    public void onScreenVisible() {
        screenVisible = true;
        if (engine != null) {
            startTicker();
        }
    }

    /** The screen is hidden: stop the timer and save. */
    public void onScreenHidden() {
        screenVisible = false;
        stopTicker();
        saveGame();
    }

    /** The Activity already showed the "boss defeated" message. */
    public void onBossDefeatedEventHandled() {
        bossDefeatedEvent.setValue(null);
    }

    private void onDamageApplied(boolean bossDefeated, String bossName) {
        if (bossDefeated) {
            bossDefeatedEvent.setValue(bossName);
            saveGame();
        }
        publishState();
    }

    private void publishState() {
        uiState.setValue(GameUiState.from(engine));
    }

    private void startTicker() {
        tickHandler.removeCallbacks(tickRunnable);
        tickHandler.postDelayed(tickRunnable, TICK_INTERVAL_MS);
    }

    private void stopTicker() {
        tickHandler.removeCallbacks(tickRunnable);
    }

    private void saveGame() {
        if (engine != null && username != null) {
            repository.savePlayer(PlayerMapper.toEntity(username, engine));
        }
    }

    /** The ViewModel is destroyed: remove the timer (no leaks) and save. */
    @Override
    protected void onCleared() {
        stopTicker();
        saveGame();
        super.onCleared();
    }

    public LiveData<GameUiState> getUiState() {
        return uiState;
    }

    public LiveData<String> getBossDefeatedEvent() {
        return bossDefeatedEvent;
    }
}
