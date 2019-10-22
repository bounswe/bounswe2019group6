package com.traderx.ui.profile

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.traderx.R
import com.traderx.adapter.UserAdapter
import com.traderx.api.ResponseHandler
import com.traderx.util.Injection
import com.traderx.viewmodel.UserViewModel
import io.reactivex.disposables.CompositeDisposable

class AuthUserProfileActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel

    private lateinit var userName: TextView
    private lateinit var email: TextView
    private lateinit var role: TextView
    private lateinit var profilePrivate: TextView

    private val disposable = CompositeDisposable()

    private val TAG = "AuthUserProfileActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_user_profile)

        userName = findViewById(R.id.profile_username)
        email = findViewById(R.id.profile_email)
        role = findViewById(R.id.profile_role)
        profilePrivate = findViewById(R.id.profile_private)

        val userViewModelFactory = Injection.provideUserViewModelFactory(this)

        userViewModel = ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        disposable.add(
            userViewModel.user().subscribe({
                runOnUiThread{
                    Log.d(TAG, it.username)
                    userName.text = it.username
                    email.text = it.email
                    role.text = getString(UserAdapter.adaptRole(it.role))
                    profilePrivate.text =
                        if (it.isPrivate) getString(R.string.general_true) else getString(R.string.general_false)
                }
            }, {
                Log.e(TAG, it.message)
            })
        )

        disposable.add(
            userViewModel.fetchAndUpdateUser().subscribe(
                {},
                { ResponseHandler.handleError(it, this) })
        )
    }

    override fun onStop() {
        super.onStop()

        disposable.clear()
    }
}
