package com.juanluis.cupheadclicker.domain;

import static org.junit.Assert.assertEquals;

import com.juanluis.cupheadclicker.ui.domain.UsernameValidator;

import org.junit.Test;

/**
 * Unit tests for {@link UsernameValidator}.
 *
 * @author Juan Luis Simancas Prundea
 * @version 1.0
 */
public class UsernameValidatorTest {

    @Test
    public void validName_isValid() {
        assertEquals(UsernameValidator.Result.VALID, UsernameValidator.validate("Juan_23"));
    }

    @Test
    public void nullOrBlank_isEmpty() {
        assertEquals(UsernameValidator.Result.EMPTY, UsernameValidator.validate(null));
        assertEquals(UsernameValidator.Result.EMPTY, UsernameValidator.validate("   "));
    }

    @Test
    public void twoCharacters_isTooShort() {
        assertEquals(UsernameValidator.Result.TOO_SHORT, UsernameValidator.validate("ab"));
    }

    @Test
    public void sixteenCharacters_isTooLong() {
        assertEquals(UsernameValidator.Result.TOO_LONG, UsernameValidator.validate("abcdefghijklmnop"));
    }

    @Test
    public void spacesOrSymbols_areInvalid() {
        assertEquals(UsernameValidator.Result.INVALID_CHARACTERS, UsernameValidator.validate("juan luis"));
        assertEquals(UsernameValidator.Result.INVALID_CHARACTERS, UsernameValidator.validate("juan!"));
    }

    @Test
    public void spacesAroundName_areIgnored() {
        assertEquals(UsernameValidator.Result.VALID, UsernameValidator.validate("  Juan  "));
    }
}
