package com.traderx.activity


import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.traderx.R
import com.traderx.api.ApiService
import com.traderx.api.RequestService
import com.traderx.api.ResponseHandler
import com.traderx.api.request.RegisterRequest
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException

class RegisterActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var userName: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var passwordConfirm: EditText
    private lateinit var warning: TextView
    private lateinit var warningLayout: ConstraintLayout
    private lateinit var registerButton: Button

    private lateinit var mMap: GoogleMap
    private var mMarker: Marker? = null

    private lateinit var requestService: RequestService
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_register_activity)

        requestService = ApiService.getInstance()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.register_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        userName = findViewById(R.id.register_username_val)
        email = findViewById(R.id.register_mail_val)
        password = findViewById(R.id.register_pass_val)
        passwordConfirm = findViewById(R.id.register_pass_confirm_val)
        warning = findViewById(R.id.register_warning)
        registerButton = findViewById(R.id.register_button)
        warningLayout = findViewById(R.id.register_warning_layout)

        registerButton.setOnClickListener {
            registerUser()
        }
    }

    override fun onStop() {
        super.onStop()

        disposable.clear()
    }

    private fun registerUser() {
        clearWarning()

        when {
            !checkUsername(userName.text.toString()) ->
                setWarning("Username is not valid")
            !checkEmail(email.text.toString()) ->
                setWarning("Email is not valid")
            !checkPassword(password.text.toString()) ->
                setWarning("Password is not valid")
            !checkPasswordConformity(password.text.toString(), passwordConfirm.text.toString()) ->
                setWarning("Passwords does not match up")
            !checkLocation(mMarker) ->
                setWarning("You did not select your location from map")
        }

        if (warningLayout.visibility != View.GONE) {
            return
        }

        disposable.add(
            requestService.register(
                RegisterRequest(
                    username = userName.text.toString(),
                    email = email.text.toString(),
                    password = password.text.toString(),
                    latitude = mMarker?.position?.latitude?.toFloat().toString(),
                    longitude = mMarker?.position?.longitude?.toFloat().toString()
                )
            )
                .subscribe({

                    runOnUiThread {
                        clearWarning()

                        AlertDialog.Builder(this)
                            .setMessage("You have successfully signed up. Please verify your email then sign in.")
                            .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, id ->
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                            }).create().show()
                    }

                }, {
                    if (!ResponseHandler.handleError(it, this)) {
                        if (it is HttpException) {
                            val serializedError = it.response().errorBody()?.string() ?: ""
                            Log.e("RegisterActivity", serializedError)
                            val errorResponse = ResponseHandler.parseErrorMessage(serializedError)

                            if (errorResponse.status == 422) {
                                runOnUiThread { setWarning(errorResponse.message) }
                            }
                        }
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

    private fun checkUsername(userName: String): Boolean {
        return userName.length in 5..20
    }

    private fun checkEmail(email: String): Boolean {
        // Credits to https://www.tutorialspoint.com/validate-email-address-in-java
        val patt = Regex(
            "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]\$"
        )

        return email.matches(patt)
    }

    private fun checkPassword(password: String): Boolean {
        return password.length in 8..32
    }

    private fun checkPasswordConformity(password: String, passwordConfirm: String): Boolean {
        return password == passwordConfirm
    }

    private fun checkLocation(marker: Marker?): Boolean {
        return marker != null
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