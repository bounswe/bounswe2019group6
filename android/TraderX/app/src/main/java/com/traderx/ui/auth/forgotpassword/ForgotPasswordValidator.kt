package com.traderx.auth.forgotpassword;

public class ForgotPasswordValidator {
    companion object{
        fun validateEmail(email: String): Boolean {
            // Credits to https://www.tutorialspoint.com/validate-email-address-in-java
            val patt = Regex(
                    "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]\$"
            )

            return email.matches(patt)
        }
    }
}