package com.traderx.ui.auth.signup

import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class SignUpValidatorTest {

    @Test
    fun validateUsername_empty() {
        assertThat(SignUpValidator.validateUsername(""), `is`(false))
    }

    @Test
    fun validateUsername_not_valid_less() {
        assertThat(SignUpValidator.validateUsername("test"), `is`(false))
    }

    @Test
    fun validateUsername_valid() {
        assertThat(SignUpValidator.validateUsername("bwqrr"), `is`(true))
    }

    @Test
    fun validateUsername_not_valid_exceed() {
        assertThat(SignUpValidator.validateUsername("testtesttesttesttesttest"), `is`(false))
    }

    @Test
    fun validateIban_empty() {
        assertThat(SignUpValidator.validateIban(""), `is`(false))
    }

    @Test
    fun validateIban_not_valid() {
        assertThat(SignUpValidator.validateIban("TR23123231"), `is`(false))
    }

    @Test
    fun validateIban_valid() {
        assertThat(SignUpValidator.validateIban("TR123456789011121314151617"), `is`(true))
    }

    @Test
    fun validateEmail_empty() {
        assertThat(SignUpValidator.validateEmail(""), `is`(false))
    }

    @Test
    fun validateEmail_not_valid1() {
        assertThat(SignUpValidator.validateEmail("test"), `is`(false))
    }

    @Test
    fun validateEmail_not_valid2() {
        assertThat(SignUpValidator.validateEmail("test@"), `is`(false))
    }

    @Test
    fun validateEmail_not_valid3() {
        assertThat(SignUpValidator.validateEmail("test@test"), `is`(false))
    }

    @Test
    fun validateEmail_not_valid4() {
        assertThat(SignUpValidator.validateEmail("test@test."), `is`(false))
    }

    @Test
    fun validateEmail_valid() {
        assertThat(SignUpValidator.validateEmail("test@test.test"), `is`(true))
    }

    @Test
    fun validatePassword_empty() {
        assertThat(SignUpValidator.validatePassword(""), `is`(false))
    }

    @Test
    fun validatePassword_not_valid_less() {
        assertThat(SignUpValidator.validatePassword("12a.56"), `is`(false))
    }

    @Test
    fun validatePassword_not_valid_exceed() {
        assertThat(
            SignUpValidator.validatePassword("1234567890111213Asdfghjklzxcvbnm."),
            `is`(false)
        )
    }

    @Test
    fun validatePassword_valid() {
        assertThat(SignUpValidator.validatePassword("Pass1234."), `is`(true))
    }

    @Test
    fun validatePasswordConformity_empty() {
        assertThat(SignUpValidator.validatePasswordConformity("", ""), `is`(true))
    }

    @Test
    fun validatePasswordConformity_not_valid() {
        assertThat(
            SignUpValidator.validatePasswordConformity("somepassword", "anotherpassword"),
            `is`(false)
        )
    }

    @Test
    fun validatePasswordConformity_valid() {
        assertThat(
            SignUpValidator.validatePasswordConformity("equalPassword", "equalPassword"),
            `is`(true)
        )
    }

    @Test
    fun validateLocation_not_valid() {
        assertThat(SignUpValidator.validateLocation(null), `is`(false))
    }

//    @Test
//    fun validateLocation_valid() {
//        assertThat(SignUpValidator.validateLocation(Marker()), `is`(true))
//    }
}