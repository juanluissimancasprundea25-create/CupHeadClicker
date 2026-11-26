package com.juanluis.cupheadclicker.ui.ranking;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.juanluis.cupheadclicker.ui.data.db.PlayerEntity;
import com.juanluis.cupheadclicker.ui.data.prefs.SessionManager;
import com.juanluis.cupheadclicker.ui.data.repository.PlayerRepository;

import java.util.List;

/**
 * Loads the ranking from the database in a background thread.
 *
 * @author Juan Luis Simancas Prundea
 * @version 1.0
 */
public class RankingViewModel extends AndroidViewModel {

    private final PlayerRepository repository;
    private final String currentUsername;
    private final MutableLiveData<List<PlayerEntity>> ranking = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    /**
     * @param application application context
     */
    public RankingViewModel(@NonNull Application application) {
        super(application);
        repository = PlayerRepository.getInstance(application);
        currentUsername = new SessionManager(application).getUsername();
    }

    /**
     * @param order order selected in the spinner
     */
    public void loadRanking(PlayerRepository.SortOrder order) {
        loading.setValue(true);
        repository.loadRanking(order, players -> {
            ranking.setValue(players);
            loading.setValue(false);
        });
    }

    /** @return name of the logged player, to highlight their row */
    public String getCurrentUsername() {
        return currentUsername;
    }

    public LiveData<List<PlayerEntity>> getRanking() {
        return ranking;
    }

    public LiveData<Boolean> isLoading() {
        return loading;
    }
}
