package com.traderx

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.traderx.activity.ForgotPassActivity
import com.traderx.activity.RegisterActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_login_activity)
        val registerButton = findViewById<Button>(R.id.login_register_button)
        val forgetButton = findViewById<Button>(R.id.login_forgot_pass)
        val password = findViewById<EditText>(R.id.login_pass_val)
        val mail = findViewById<EditText>(R.id.login_mail_val)
        val loginButton = findViewById<Button>(R.id.login_button)

        forgetButton.setOnClickListener {
            val intent = Intent(this, ForgotPassActivity::class.java)
            startActivity(intent)
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
