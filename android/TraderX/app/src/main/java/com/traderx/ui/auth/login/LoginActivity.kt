package com.traderx.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.isProgressActive
import com.github.razir.progressbutton.showProgress
import com.traderx.MainActivity
import com.traderx.R
import com.traderx.api.ApiService
import com.traderx.api.ErrorHandler
import com.traderx.api.RequestService
import com.traderx.api.request.LoginRequest
import com.traderx.auth.forgotpassword.ForgotPasswordActivity
import com.traderx.ui.auth.signup.SignUpActivity
import com.traderx.ui.auth.signup.SignUpValidator
import com.traderx.util.TokenUtility
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException

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

        findViewById<Button>(R.id.login_button)?.apply {
            this.setOnClickListener {
                login(this)
            }
        }

        findViewById<TextView>(R.id.login_forgot_pass_text).let {
            it.setOnClickListener {
                startActivity(
                    Intent(
                        this,
                        ForgotPasswordActivity::class.java
                    )
                )
            }
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

    private fun login(button: Button) {

        if (button.isProgressActive()) {
            return
        }

        clearWarning()

        when {
            !SignUpValidator.validateUsername(username.text.toString()) ->
                setWarning(getString(R.string.username_not_valid))
        }

        if (warningLayout.visibility != View.GONE) {
            return
        }

        button.showProgress()

        disposable.add(
            requestService.login(
                LoginRequest(
                    username = username.text.toString(),
                    password = password.text.toString()
                )
            ).observeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    button.hideProgress(R.string.login)
                }
                .subscribe({
                    TokenUtility.storeToken(it.token, this)

                    startActivity(Intent(this, MainActivity::class.java))
                }, {

                    if (!ErrorHandler.handleConnectException(it, this) && it is HttpException) {

                        val errorResponse = ErrorHandler.parseErrorMessage(
                            it.response().errorBody()?.string() ?: ""
                        )

                        setWarning(errorResponse.message)
                    }
                })
        )
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
