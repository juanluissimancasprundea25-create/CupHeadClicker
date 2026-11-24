package com.juanluis.cupheadclicker.ui.ranking;   // ui.ranking / ui.contact

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.juanluis.cupheadclicker.R;

/**
 * Game screen (layout only).
 *
 * @author Juan Luis Simancas Prundea
 * @version 0.1
 */
public class RankingActivity extends AppCompatActivity {   // RankingActivity / ContactActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);        // activity_ranking / activity_contact
    }
}