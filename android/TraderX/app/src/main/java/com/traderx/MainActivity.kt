package com.traderx

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.traderx.api.ResponseHandler
import com.traderx.util.Injection
import com.traderx.viewmodel.UserViewModel
import io.reactivex.disposables.CompositeDisposable

class MainActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel

    private lateinit var userNameTextView: TextView
    private lateinit var userIdTextView: TextView

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userIdTextView = findViewById(R.id.userId)
        userNameTextView = findViewById(R.id.userName)

        val userViewModelFactory = Injection.provideUserViewModelFactory(this)

        userViewModel = ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        disposable.add(
            userViewModel.user()
                .subscribe ({
                    userNameTextView.text = it.username
                    userIdTextView.text = it.id.toString()
                },
                    {
                        ResponseHandler.handleError(it, this)
                    }
                )
        )

        disposable.add(
            userViewModel
                .fetchAndUpdateUser()
                .subscribe({}, {
                    ResponseHandler.handleError(it, this)
                })
        )
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }
}
