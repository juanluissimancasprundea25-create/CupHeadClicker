package com.juanluis.cupheadclicker.ui.domain;

/**
 * Validates the username typed by the player on the login screen.
 * <p>
 * Rules: it cannot be empty, it must have between {@link #MIN_LENGTH} and
 * {@link #MAX_LENGTH} characters and it can only contain letters, digits or
 * underscores. This class has no Android dependencies, so it can be tested
 * with plain JUnit tests on the JVM.
 * </p>
 *
 * @author Juan Luis Simancas Prundea
 * @version 1.0
 */
public final class UsernameValidator {

    /** Possible outcomes of a username validation. */
    public enum Result {
        /** The username is correct. */
        VALID,
        /** The field is empty or only has blank spaces. */
        EMPTY,
        /** The username is shorter than {@link #MIN_LENGTH}. */
        TOO_SHORT,
        /** The username is longer than {@link #MAX_LENGTH}. */
        TOO_LONG,
        /** The username has characters that are not allowed. */
        INVALID_CHARACTERS
    }

    /** Minimum number of characters of a username. */
    public static final int MIN_LENGTH = 3;

    /** Maximum number of characters of a username. */
    public static final int MAX_LENGTH = 15;

    /** Regular expression with the allowed characters. */
    private static final String ALLOWED_PATTERN = "[A-Za-z0-9_]+";

    /** Utility class: it must not be instantiated. */
    private UsernameValidator() {
    }

    /**
     * Checks if a username follows all the rules.
     *
     * @param username text typed by the user (it can be {@code null})
     * @return the validation {@link Result}
     */
    public static Result validate(String username) {
        if (username == null || username.trim().isEmpty()) {
            return Result.EMPTY;
        }
        String clean = username.trim();
        if (clean.length() < MIN_LENGTH) {
            return Result.TOO_SHORT;
        }
        if (clean.length() > MAX_LENGTH) {
            return Result.TOO_LONG;
        }
        if (!clean.matches(ALLOWED_PATTERN)) {
            return Result.INVALID_CHARACTERS;
        }
        return Result.VALID;
    }
}
