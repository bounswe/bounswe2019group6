package com.traderx.auth.forgotpassword

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
import com.traderx.api.request.ForgotPasswordRequest
import com.traderx.api.request.LoginRequest
import com.traderx.ui.auth.signup.SignUpActivity
import com.traderx.ui.auth.login.LoginActivity
import com.traderx.ui.auth.signup.SignUpValidator
import com.traderx.util.TokenUtility
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException


class ForgotPasswordActivity: AppCompatActivity() {
    private lateinit var email: EditText


    private lateinit var warning: TextView
    private lateinit var warningLayout: ConstraintLayout
    private val disposable = CompositeDisposable()



    private lateinit var requestService: RequestService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_forgot_activity)

        findViewById<TextView>(R.id.forgot_password_signup_text).let { view ->
            view.setOnClickListener {
                startActivity(Intent(this, SignUpActivity::class.java))
            }
        }

        findViewById<Button>(R.id.reset_password_button)?.apply {
            this.setOnClickListener {
                resetPassword(this)
            }
        }

        findViewById<TextView>(R.id.forgot_password_login_text).let {
            it.setOnClickListener {
                startActivity(
                    Intent(
                        this,
                        LoginActivity::class.java
                    )
                )
            }
        }

        requestService = ApiService.getInstance()

        email = findViewById(R.id.forgot_password_email)

        warning = findViewById(R.id.forgot_password_warning)
        warningLayout = findViewById(R.id.forgot_password_warning_layout)
    }

    override fun onStop() {
        super.onStop()

        disposable.clear()
    }

    private fun resetPassword(button: Button) {

        if (button.isProgressActive()) {
            return
        }

        clearWarning()

        when {
            !ForgotPasswordValidator.validateEmail(email.text.toString()) ->{
                setWarning(getString(R.string.email_not_valid))
            }
        }

        if (warningLayout.visibility != View.GONE) {
            return
        }

        button.showProgress()

        disposable.add(
            requestService.forgotpassword(
               email = email.text.toString()
            ).observeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    button.hideProgress(R.string.reset_password) //?????????????????????????????????
                }
                .subscribe({
                    Toast.makeText(this,"Reset mail sent", Toast.LENGTH_SHORT ).show()
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