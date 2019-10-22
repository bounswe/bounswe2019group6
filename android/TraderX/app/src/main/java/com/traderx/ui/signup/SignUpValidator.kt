package com.traderx.ui.signup

import com.google.android.gms.maps.model.Marker

class SignUpValidator {
    companion object {
        fun validateIban(iban: String): Boolean {
            val patt = Regex("^[A-Z]{2}[0-9]{24}\$")

            return iban.matches(patt)
        }

        fun validateUsername(userName: String): Boolean {
            val patt = Regex("^\\w{3,20}$")

            return userName.matches(patt)
        }

        fun validateEmail(email: String): Boolean {
            val patt = Regex("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]\$")

            return email.matches(patt)
        }

        fun validatePassword(password: String): Boolean {
            val patt = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=])(?=\\S+\$).{6,}\$")

            return password.matches(patt)
        }

        fun validatePasswordConformity(password: String, passwordConfirm: String): Boolean {
            return password == passwordConfirm
        }

        fun validateLocation(marker: Marker?): Boolean {
            return marker != null
        }

    }
}