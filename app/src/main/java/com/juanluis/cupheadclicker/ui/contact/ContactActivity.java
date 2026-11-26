package com.juanluis.cupheadclicker.ui.contact;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.juanluis.cupheadclicker.R;
import com.juanluis.cupheadclicker.ui.domain.ContactValidator;

/**
 * Contact form. It validates the fields with {@link ContactValidator} and
 * opens the email app of the device with the message ready to send.
 *
 * @author Juan Luis Simancas Prundea
 * @version 1.0
 */
public class ContactActivity extends AppCompatActivity {

    private TextInputLayout nameLayout;
    private TextInputLayout emailLayout;
    private TextInputLayout messageLayout;
    private TextInputEditText nameInput;
    private TextInputEditText emailInput;
    private TextInputEditText messageInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        // Closes this screen and goes back to the menu, which is still in the back stack
        findViewById(R.id.button_back).setOnClickListener(v -> finish());

        nameLayout = findViewById(R.id.layout_contact_name);
        emailLayout = findViewById(R.id.layout_contact_email);
        messageLayout = findViewById(R.id.layout_contact_message);
        nameInput = findViewById(R.id.input_contact_name);
        emailInput = findViewById(R.id.input_contact_email);
        messageInput = findViewById(R.id.input_contact_message);

        findViewById(R.id.button_send).setOnClickListener(v -> onSendClicked());
    }

    private void onSendClicked() {
        String name = textOf(nameInput);
        String email = textOf(emailInput);
        String message = textOf(messageInput);

        boolean valid = showError(nameLayout, ContactValidator.isValidName(name),
                getString(R.string.error_contact_name));
        valid &= showError(emailLayout, ContactValidator.isValidEmail(email),
                getString(R.string.error_contact_email));
        valid &= showError(messageLayout, ContactValidator.isValidMessage(message),
                getString(R.string.error_contact_message,
                        ContactValidator.MIN_MESSAGE_LENGTH, ContactValidator.MAX_MESSAGE_LENGTH));

        if (!valid) {
            Toast.makeText(this, R.string.contact_fix_errors, Toast.LENGTH_SHORT).show();
            return;
        }
        sendEmail(name.trim(), email.trim(), message.trim());
    }

    /**
     * Shows or hides the error of a field.
     *
     * @return the same {@code isValid} value, to chain the checks
     */
    private boolean showError(TextInputLayout layout, boolean isValid, String error) {
        layout.setError(isValid ? null : error);
        return isValid;
    }

    /** Opens the email app with an implicit Intent. */
    private void sendEmail(String name, String email, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.company_email)});
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contact_email_subject, name));
        intent.putExtra(Intent.EXTRA_TEXT, message + "\n\n" + name + " <" + email + ">");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.contact_no_email_app, Toast.LENGTH_LONG).show();
        }
    }

    private static String textOf(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString();
    }
}
