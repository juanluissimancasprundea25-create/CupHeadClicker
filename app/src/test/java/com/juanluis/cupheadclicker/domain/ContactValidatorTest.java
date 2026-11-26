package com.juanluis.cupheadclicker.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.juanluis.cupheadclicker.ui.domain.ContactValidator;

import org.junit.Test;

/**
 * Unit tests for {@link ContactValidator}.
 *
 * @author Juan Luis Simancas Prundea
 * @version 1.0
 */
public class ContactValidatorTest {

    @Test
    public void name_needsTwoCharacters() {
        assertTrue(ContactValidator.isValidName("Jl"));
        assertFalse(ContactValidator.isValidName("J"));
        assertFalse(ContactValidator.isValidName(null));
    }

    @Test
    public void email_mustHaveValidFormat() {
        assertTrue(ContactValidator.isValidEmail("juan@gmail.com"));
        assertFalse(ContactValidator.isValidEmail("juangmail.com"));
        assertFalse(ContactValidator.isValidEmail("juan@gmail"));
        assertFalse(ContactValidator.isValidEmail(""));
    }

    @Test
    public void message_mustRespectLengthLimits() {
        assertTrue(ContactValidator.isValidMessage("Me encanta el juego"));
        assertFalse(ContactValidator.isValidMessage("Hola"));
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < ContactValidator.MAX_MESSAGE_LENGTH + 1; i++) {
            longText.append('a');
        }
        assertFalse(ContactValidator.isValidMessage(longText.toString()));
    }
}
