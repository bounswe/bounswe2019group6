package com.traderx.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.traderx.MainActivity
import com.traderx.R

class ForgotPassActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_forgot_activity)


        val mail = findViewById<EditText>(R.id.forgot_password_mail_val)

        val resetButton = findViewById<Button>(R.id.reset_password_button)
        val forgetPasswordBackButton = findViewById<Button>(R.id.forgot_password_back_button)

        resetButton.setOnClickListener {
        }

        forgetPasswordBackButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}