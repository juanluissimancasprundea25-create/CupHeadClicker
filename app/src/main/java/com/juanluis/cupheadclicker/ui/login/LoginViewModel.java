package com.juanluis.cupheadclicker.ui.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.juanluis.cupheadclicker.ui.data.prefs.SessionManager;
import com.juanluis.cupheadclicker.ui.data.repository.PlayerRepository;
import com.juanluis.cupheadclicker.ui.domain.UsernameValidator;

/**
 * Logic of the login screen: validates the name, creates the player in the
 * database and saves the session. It survives screen rotations.
 *
 * @author Juan Luis Simancas Prundea
 * @version 1.0
 */
public class LoginViewModel extends AndroidViewModel {

    private final PlayerRepository repository;
    private final SessionManager sessionManager;

    private final MutableLiveData<UsernameValidator.Result> validationResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    /**
     * @param application application context (no Activity, so no leaks)
     */
    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository = PlayerRepository.getInstance(application);
        sessionManager = new SessionManager(application);
    }

    /** @return {@code true} if a user was already logged */
    public boolean hasActiveSession() {
        return sessionManager.isLoggedIn();
    }

    /**
     * Validates the name and, if it is correct, logs the player in.
     *
     * @param rawUsername text typed by the user
     */
    public void login(String rawUsername) {
        UsernameValidator.Result result = UsernameValidator.validate(rawUsername);
        validationResult.setValue(result);
        if (result != UsernameValidator.Result.VALID) {
            return;
        }
        String username = rawUsername.trim();
        loading.setValue(true);
        repository.loginPlayer(username, player -> {
            sessionManager.saveUsername(username);
            loading.setValue(false);
            loginSuccess.setValue(true);
        });
    }

    public LiveData<UsernameValidator.Result> getValidationResult() {
        return validationResult;
    }

    public LiveData<Boolean> getLoginSuccess() {
        return loginSuccess;
    }

    public LiveData<Boolean> isLoading() {
        return loading;
    }
}
