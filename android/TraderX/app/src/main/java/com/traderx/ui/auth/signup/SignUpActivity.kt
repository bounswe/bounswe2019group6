package com.traderx.ui.auth.signup


import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.isProgressActive
import com.github.razir.progressbutton.showProgress
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.traderx.R
import com.traderx.api.ApiService
import com.traderx.api.ErrorHandler
import com.traderx.api.RequestService
import com.traderx.api.request.SignUpRequest
import com.traderx.ui.auth.login.LoginActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException

class SignUpActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var userName: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var passwordConfirm: EditText
    private lateinit var warning: TextView
    private lateinit var warningLayout: ConstraintLayout
    private lateinit var ibanCheckbox: CheckBox
    private lateinit var iban: EditText
    private lateinit var mMap: GoogleMap
    private var mMarker: Marker? = null

    private lateinit var requestService: RequestService
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_signup_activity)

        requestService = ApiService.getInstance()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.signup_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        userName = findViewById(R.id.signup_username)
        email = findViewById(R.id.signup_mail)
        password = findViewById(R.id.signup_pass)
        passwordConfirm = findViewById(R.id.signup_pass_confirm)
        ibanCheckbox = findViewById<CheckBox>(R.id.signup_iban_checkbox).also {
            it.setOnCheckedChangeListener { compound: CompoundButton, checked: Boolean ->
                changeIbanEdit(iban, checked)
            }
        }

        iban = findViewById(R.id.signup_iban)
        warning = findViewById(R.id.signup_warning)
        warningLayout = findViewById(R.id.signup_warning_layout)

        //Disable iban editable
        changeIbanEdit(iban, false)

        findViewById<Button>(R.id.signup_button)?.apply {
            setOnClickListener {
                signUpUser(this)
            }
        }
    }

    override fun onStop() {
        super.onStop()

        disposable.clear()
    }

    private fun signUpUser(button: Button) {

        if (button.isProgressActive()) {
            return
        }

        clearWarning()

        when {
            !SignUpValidator.validateUsername(userName.text.toString()) ->
                setWarning(getString(R.string.username_not_valid))
            !SignUpValidator.validateEmail(email.text.toString()) ->
                setWarning(getString(R.string.email_not_valid))
            !SignUpValidator.validatePassword(password.text.toString()) ->
                setWarning(getString(R.string.pass_not_valid))
            !SignUpValidator.validatePasswordConformity(
                password.text.toString(),
                passwordConfirm.text.toString()
            ) ->
                setWarning(getString(R.string.pass_match_fail))
            !SignUpValidator.validateLocation(mMarker) ->
                setWarning(getString(R.string.select_location))
            ibanCheckbox.isChecked && !SignUpValidator.validateIban(iban.text.toString()) ->
                setWarning(getString(R.string.trader_iban_not_valid))
        }

        if (warningLayout.visibility != View.GONE) {
            return
        }

        button.showProgress()

        disposable.add(
            requestService.register(
                SignUpRequest(
                    username = userName.text.toString(),
                    email = email.text.toString(),
                    password = password.text.toString(),
                    latitude = mMarker?.position?.latitude?.toFloat().toString(),
                    longitude = mMarker?.position?.longitude?.toFloat().toString(),
                    iban = if (ibanCheckbox.isChecked) iban.text.toString() else null
                )
            )
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    button.hideProgress(R.string.signup)
                }
                .subscribe({
                    clearWarning()

                    AlertDialog.Builder(this)
                        .setMessage(getString(R.string.signup_success))
                        .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, id ->
                            startActivity(Intent(this, LoginActivity::class.java))
                        }).create().show()

                }, {
                    if (!ErrorHandler.handleError(it, this)) {
                        if (it is HttpException) {
                            val serializedError = it.response().errorBody()?.string() ?: ""

                            val errorResponse = ErrorHandler.parseErrorMessage(serializedError)

                            if (errorResponse.status == 422 || errorResponse.status == 412 || errorResponse.status == 500) {
                                setWarning(errorResponse.message)
                            }
                        }
                    }
                })
        )
    }

    private fun changeIbanEdit(iban: EditText, checked: Boolean) {
        iban.inputType = if (checked) InputType.TYPE_CLASS_TEXT else InputType.TYPE_NULL
        iban.isFocusable = checked
        iban.isFocusableInTouchMode = checked
    }

    private fun setWarning(warning: String) {
        this.warning.text = warning
        warningLayout.visibility = View.VISIBLE
    }

    private fun clearWarning() {
        warning.text = ""
        warningLayout.visibility = View.GONE
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Set main camera to Turkey
        val turkey = LatLng(39.0905013, 35.4270658)

        mMap.moveCamera(CameraUpdateFactory.newLatLng(turkey))

        mMap.setOnMapLongClickListener {
            mMarker?.let { marker -> marker.remove() }

            mMarker = mMap.addMarker(MarkerOptions().position(it).title("Your Location"))
        }
    }
}