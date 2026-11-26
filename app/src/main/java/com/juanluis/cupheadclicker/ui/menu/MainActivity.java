package com.juanluis.cupheadclicker.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.juanluis.cupheadclicker.R;
import com.juanluis.cupheadclicker.ui.data.prefs.SessionManager;
import com.juanluis.cupheadclicker.ui.contact.ContactActivity;
import com.juanluis.cupheadclicker.ui.game.GameActivity;
import com.juanluis.cupheadclicker.ui.login.LoginActivity;
import com.juanluis.cupheadclicker.ui.ranking.RankingActivity;

/**
 * Main menu: play, ranking, contact and change user.
 *
 * @author Juan Luis Simancas Prundea
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);

        String username = sessionManager.getUsername();
        if (username == null) {
            goToLogin();
            return;
        }

        setContentView(R.layout.activity_main);
        TextView greeting = findViewById(R.id.text_greeting);
        greeting.setText(getString(R.string.menu_greeting, username));

        findViewById(R.id.button_play).setOnClickListener(v ->
                startActivity(new Intent(this, GameActivity.class)));
        findViewById(R.id.button_ranking).setOnClickListener(v ->
                startActivity(new Intent(this, RankingActivity.class)));
        findViewById(R.id.button_contact).setOnClickListener(v ->
                startActivity(new Intent(this, ContactActivity.class)));
        findViewById(R.id.button_logout).setOnClickListener(v -> {
            sessionManager.logout();
            goToLogin();
        });
    }

    /** Opens the login screen and closes the menu. */
    private void goToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
