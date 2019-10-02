package com.traderx

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.traderx.api.RequestServiceFactory
import com.traderx.api.ResponseHandler
import com.traderx.db.User
import com.traderx.util.Injection
import com.traderx.viewmodel.UserViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    userNameTextView.text = it.name
                    userIdTextView.text = it.id.toString()
                }
        )

        disposable.add(
            userViewModel
                .fetchAndUpdateUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, {
                    ResponseHandler<Throwable>().handleError(it, this)
                })
        )
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }
}
