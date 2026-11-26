package com.juanluis.cupheadclicker.ui.domain;

/**
 * Validates the fields of the contact form (name, email and message).
 * <p>
 * It does not use {@code android.util.Patterns} on purpose, so the rules can
 * be checked with plain JUnit tests without an emulator.
 * </p>
 *
 * @author Juan Luis Simancas Prundea
 * @version 1.0
 */
public final class ContactValidator {

    /** Minimum length of the sender name. */
    public static final int MIN_NAME_LENGTH = 2;

    /** Minimum length of the message. */
    public static final int MIN_MESSAGE_LENGTH = 10;

    /** Maximum length of the message. */
    public static final int MAX_MESSAGE_LENGTH = 500;

    /** Simple email pattern: text, @, domain, dot and extension. */
    private static final String EMAIL_PATTERN =
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    /** Utility class: it must not be instantiated. */
    private ContactValidator() {
    }

    /**
     * @param name sender name
     * @return {@code true} if the name has at least {@link #MIN_NAME_LENGTH} characters
     */
    public static boolean isValidName(String name) {
        return name != null && name.trim().length() >= MIN_NAME_LENGTH;
    }

    /**
     * @param email sender email
     * @return {@code true} if the email has a valid format
     */
    public static boolean isValidEmail(String email) {
        return email != null && email.trim().matches(EMAIL_PATTERN);
    }

    /**
     * @param message text of the message
     * @return {@code true} if the length is between the minimum and the maximum
     */
    public static boolean isValidMessage(String message) {
        if (message == null) {
            return false;
        }
        int length = message.trim().length();
        return length >= MIN_MESSAGE_LENGTH && length <= MAX_MESSAGE_LENGTH;
    }
}
