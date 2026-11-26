package com.juanluis.cupheadclicker.ui.game;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.juanluis.cupheadclicker.R;

/**
 * Game screen: the player taps the boss to hit it and buys upgrades.
 * It only paints the {@link GameUiState} and sends the clicks to
 * {@link GameViewModel}.
 *
 * @author Juan Luis Simancas Prundea
 * @version 1.0
 */
public class GameActivity extends AppCompatActivity {

    private static final float HIT_SCALE = 0.9f;
    private static final long HIT_ANIMATION_MS = 120L;

    private GameViewModel viewModel;

    private TextView levelText;
    private TextView rankText;
    private TextView coinsText;
    private TextView scoreText;
    private TextView bossNameText;
    private TextView bossHpText;
    private TextView statsText;
    private ProgressBar bossHpBar;
    private View bossContainer;
    private View bossBody;
    private Button tapUpgradeButton;
    private Button autoUpgradeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        viewModel = new ViewModelProvider(this).get(GameViewModel.class);

        bindViews();

        bossContainer.setOnClickListener(v -> {
            viewModel.onBossTapped();
            playHitAnimation(v);
        });
        tapUpgradeButton.setOnClickListener(v -> viewModel.onBuyTapUpgrade());
        autoUpgradeButton.setOnClickListener(v -> viewModel.onBuyAutoUpgrade());

        viewModel.getUiState().observe(this, this::render);
        viewModel.getBossDefeatedEvent().observe(this, bossName -> {
            if (bossName != null) {
                Toast.makeText(this, getString(R.string.game_boss_defeated, bossName),
                        Toast.LENGTH_SHORT).show();
                viewModel.onBossDefeatedEventHandled();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.onScreenVisible();
    }

    @Override
    protected void onPause() {
        viewModel.onScreenHidden();
        super.onPause();
    }

    private void bindViews() {
        levelText = findViewById(R.id.text_level);
        rankText = findViewById(R.id.text_rank);
        coinsText = findViewById(R.id.text_coins);
        scoreText = findViewById(R.id.text_score);
        bossNameText = findViewById(R.id.text_boss_name);
        bossHpText = findViewById(R.id.text_boss_hp);
        statsText = findViewById(R.id.text_stats);
        bossHpBar = findViewById(R.id.progress_boss_hp);
        bossContainer = findViewById(R.id.boss_container);
        bossBody = findViewById(R.id.view_boss_body);
        tapUpgradeButton = findViewById(R.id.button_upgrade_tap);
        autoUpgradeButton = findViewById(R.id.button_upgrade_auto);
    }

    /**
     * Paints the current state of the game.
     *
     * @param state values to show
     */
    private void render(GameUiState state) {
        levelText.setText(getString(R.string.game_level, state.level));
        rankText.setText(state.rankTitle);
        coinsText.setText(getString(R.string.game_coins, state.coins));
        scoreText.setText(getString(R.string.game_score, state.score));
        bossNameText.setText(state.bossName);
        bossHpText.setText(getString(R.string.game_boss_hp, state.bossHp, state.bossMaxHp));
        bossHpBar.setProgress(state.getHpPercent());
        bossBody.setBackgroundTintList(ColorStateList.valueOf(state.bossColor));
        statsText.setText(getString(R.string.game_stats, state.tapDamage, state.autoDamage));
        tapUpgradeButton.setText(getString(R.string.game_upgrade_tap, state.tapUpgradeCost));
        autoUpgradeButton.setText(getString(R.string.game_upgrade_auto, state.autoUpgradeCost));
        tapUpgradeButton.setEnabled(state.canBuyTapUpgrade);
        autoUpgradeButton.setEnabled(state.canBuyAutoUpgrade);
    }

    /** Small "squash" animation when the boss is hit. */
    private void playHitAnimation(View view) {
        view.animate().cancel();
        view.setScaleX(HIT_SCALE);
        view.setScaleY(HIT_SCALE);
        view.animate().scaleX(1f).scaleY(1f).setDuration(HIT_ANIMATION_MS).start();
    }
}
