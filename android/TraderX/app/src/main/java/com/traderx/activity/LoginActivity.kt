package com.traderx.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.traderx.MainActivity
import com.traderx.R
import com.traderx.api.RequestService
import com.traderx.api.RequestServiceFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginActivity : AppCompatActivity() {

    private lateinit var requestService : RequestService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_login_activity)
        val registerButton = findViewById<Button>(R.id.login_register_button)
        val forgetButton = findViewById<Button>(R.id.login_forgot_pass)
        val password = findViewById<EditText>(R.id.login_pass_val)
        val mail = findViewById<EditText>(R.id.login_mail_val)
        val loginButton = findViewById<Button>(R.id.login_button)

        requestService = RequestServiceFactory().provideRequestService()

        forgetButton.setOnClickListener {
            val intent = Intent(this, ForgotPassActivity::class.java)
            startActivity(intent)
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            requestService.login(mail.text.toString(), password.text.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    //Store the token
                    TODO()
                    // Go to MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                }
        }
    }
}
