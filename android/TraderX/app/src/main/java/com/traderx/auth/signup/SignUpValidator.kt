package com.traderx.auth.signup

import com.google.android.gms.maps.model.Marker

class SignUpValidator {
    companion object {
        fun validateIban(iban: String): Boolean {
            val patt = Regex("^[A-Z]{2}[0-9]{24}\$")

            return iban.matches(patt)
        }

        fun validateUsername(userName: String): Boolean {
            return userName.length in 5..20
        }

        fun validateEmail(email: String): Boolean {
            // Credits to https://www.tutorialspoint.com/validate-email-address-in-java
            val patt = Regex(
                "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]\$"
            )

            return email.matches(patt)
        }

        fun validatePassword(password: String): Boolean {
            return password.length in 8..32
        }

        fun validatePasswordConformity(password: String, passwordConfirm: String): Boolean {
            return password == passwordConfirm
        }

        fun validateLocation(marker: Marker?): Boolean {
            return marker != null
        }

    }
}