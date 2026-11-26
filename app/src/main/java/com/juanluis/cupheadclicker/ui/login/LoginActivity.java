package com.juanluis.cupheadclicker.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.juanluis.cupheadclicker.R;
import com.juanluis.cupheadclicker.ui.domain.UsernameValidator;
import com.juanluis.cupheadclicker.ui.menu.MainActivity;

/**
 * First screen: the player types a username. It only has view code; the
 * logic is in {@link LoginViewModel}.
 *
 * @author Juan Luis Simancas Prundea
 * @version 1.0
 */
public class LoginActivity extends AppCompatActivity {

    private LoginViewModel viewModel;
    private TextInputLayout usernameLayout;
    private TextInputEditText usernameInput;
    private Button loginButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // If there is a saved session we skip the login screen.
        if (viewModel.hasActiveSession()) {
            goToMenu();
            return;
        }

        setContentView(R.layout.activity_login);
        usernameLayout = findViewById(R.id.layout_username);
        usernameInput = findViewById(R.id.input_username);
        loginButton = findViewById(R.id.button_login);
        progressBar = findViewById(R.id.progress_login);

        loginButton.setOnClickListener(v -> viewModel.login(getTypedUsername()));

        viewModel.getValidationResult().observe(this, this::showValidation);
        viewModel.isLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            loginButton.setEnabled(!isLoading);
        });
        viewModel.getLoginSuccess().observe(this, success -> {
            if (success) {
                goToMenu();
            }
        });
    }

    /** @return text of the username field */
    private String getTypedUsername() {
        return usernameInput.getText() == null ? "" : usernameInput.getText().toString();
    }

    /**
     * Shows the validation error under the field (feedback for the user).
     *
     * @param result result of the validation
     */
    private void showValidation(UsernameValidator.Result result) {
        if (result == null || result == UsernameValidator.Result.VALID) {
            usernameLayout.setError(null);
            return;
        }
        usernameLayout.setError(getErrorMessage(result));
    }

    /**
     * @param result validation error
     * @return text of the error in the user language
     */
    private String getErrorMessage(UsernameValidator.Result result) {
        switch (result) {
            case EMPTY:
                return getString(R.string.error_username_empty);
            case TOO_SHORT:
                return getString(R.string.error_username_too_short, UsernameValidator.MIN_LENGTH);
            case TOO_LONG:
                return getString(R.string.error_username_too_long, UsernameValidator.MAX_LENGTH);
            case INVALID_CHARACTERS:
            default:
                return getString(R.string.error_username_invalid);
        }
    }

    /** Opens the main menu and closes this screen. */
    private void goToMenu() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
