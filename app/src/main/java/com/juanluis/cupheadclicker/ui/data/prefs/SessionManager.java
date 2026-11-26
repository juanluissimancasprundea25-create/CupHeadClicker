package com.juanluis.cupheadclicker.ui.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

/**
 * Saves the logged user in SharedPreferences, so the player does not have to
 * type the name every time the app is opened.
 *
 * @author Juan Luis Simancas Prundea
 * @version 1.0
 */
public class SessionManager {

    private static final String PREFS_NAME = "cuphead_session";
    private static final String KEY_USERNAME = "username";

    private final SharedPreferences preferences;

    /**
     * @param context any context; only its application context is used
     */
    public SessionManager(Context context) {
        preferences = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Saves the username. {@code apply()} writes to disk in the background.
     *
     * @param username name of the logged player
     */
    public void saveUsername(String username) {
        preferences.edit().putString(KEY_USERNAME, username).apply();
    }

    /** @return the logged username, or {@code null} if nobody is logged */
    @Nullable
    public String getUsername() {
        return preferences.getString(KEY_USERNAME, null);
    }

    /** @return {@code true} if there is a saved session */
    public boolean isLoggedIn() {
        return getUsername() != null;
    }

    /** Removes the session (change user). The progress stays in the database. */
    public void logout() {
        preferences.edit().remove(KEY_USERNAME).apply();
    }
}
