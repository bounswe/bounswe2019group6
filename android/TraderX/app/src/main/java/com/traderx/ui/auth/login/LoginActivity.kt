package com.traderx.ui.auth.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton
import com.traderx.BuildConfig
import com.traderx.MainActivity
import com.traderx.R
import com.traderx.api.ApiService
import com.traderx.api.RequestService
import com.traderx.api.ErrorHandler
import com.traderx.api.request.LoginRequest
import com.traderx.ui.auth.signup.SignUpActivity
import com.traderx.ui.auth.signup.SignUpValidator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class LoginActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var warning: TextView
    private lateinit var warningLayout: ConstraintLayout
    private val disposable = CompositeDisposable()
    private lateinit var requestService: RequestService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<TextView>(R.id.login_signup_text).let { view ->
            view.setOnClickListener {
                startActivity(Intent(this, SignUpActivity::class.java))
            }
        }

        findViewById<MaterialButton>(R.id.login_button).let {
            it.setOnClickListener { login() }
        }

        requestService = ApiService.getInstance()

        username = findViewById(R.id.login_username)
        password = findViewById(R.id.login_password)
        warning = findViewById(R.id.login_warning)
        warningLayout = findViewById(R.id.login_warning_layout)
    }

    override fun onStop() {
        super.onStop()

        disposable.clear()
    }

    private fun login() {
        clearWarning()

        when {
            !SignUpValidator.validateUsername(username.text.toString()) ->
                setWarning(getString(R.string.username_not_valid))
        }

        if (warningLayout.visibility != View.GONE) {
            return
        }

        disposable.add(
            requestService.login(
                LoginRequest(
                    username = username.text.toString(),
                    password = password.text.toString()
                )
            ).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    storeToken(it.token)
                    ApiService.refreshInstance()
                    startActivity(Intent(this, MainActivity::class.java))
                }, { ErrorHandler.handleError(it, this) })
        )
    }

    private fun storeToken(token: String) {
        val editor = getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE).edit()

        editor.putString("token", token)

        editor.apply()
    }

    private fun setWarning(warning: String) {
        this.warning.text = warning
        warningLayout.visibility = View.VISIBLE
    }

    private fun clearWarning() {
        warning.text = ""
        warningLayout.visibility = View.GONE
    }
}
