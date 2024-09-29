package com.example.userapp

import org.junit.Assert.*
import org.junit.Test
import java.util.regex.Pattern

class RegisterValidationTest {

    // Reemplazar la validación de correo electrónico con una expresión regular
    private fun isEmailValid(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        return Pattern.compile(emailRegex).matcher(email).matches()
    }

    private fun arePasswordsMatching(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

    @Test
    fun emailValidation_isCorrect() {
        val validEmail = "test@example.com"
        val invalidEmail = "invalid-email"

        assertTrue(isEmailValid(validEmail))
        assertFalse(isEmailValid(invalidEmail))
    }

    @Test
    fun passwordsMatching_isCorrect() {
        val password = "password123"
        val confirmPassword = "password123"
        val incorrectConfirmPassword = "password321"

        assertTrue(arePasswordsMatching(password, confirmPassword))
        assertFalse(arePasswordsMatching(password, incorrectConfirmPassword))
    }
}
