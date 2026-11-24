package com.juanluis.cupheadclicker.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.juanluis.cupheadclicker.R;
import com.juanluis.cupheadclicker.ui.menu.MainActivity;

/**
 * Login screen (navigation only, logic will be added later).
 *
 * @author Juan Luis Simancas Prundea
 * @version 0.1
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.button_login).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}