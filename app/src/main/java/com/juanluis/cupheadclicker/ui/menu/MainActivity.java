package com.juanluis.cupheadclicker.ui.menu;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.juanluis.cupheadclicker.R;
import com.juanluis.cupheadclicker.ui.contact.ContactActivity;
import com.juanluis.cupheadclicker.ui.game.GameActivity;
import com.juanluis.cupheadclicker.ui.login.LoginActivity;
import com.juanluis.cupheadclicker.ui.ranking.RankingActivity;

/**
 * Main menu (navigation only).
 *
 * @author Juan Luis Simancas Prundea
 * @version 0.1
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button_play).setOnClickListener(v ->
                startActivity(new Intent(this, GameActivity.class)));
        findViewById(R.id.button_ranking).setOnClickListener(v ->
                startActivity(new Intent(this, RankingActivity.class)));
        findViewById(R.id.button_contact).setOnClickListener(v ->
                startActivity(new Intent(this, ContactActivity.class)));
        findViewById(R.id.button_logout).setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}