package com.traderx.ui.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.isProgressActive
import com.github.razir.progressbutton.showProgress
import com.google.android.material.snackbar.Snackbar
import com.traderx.R
import com.traderx.api.ApiService
import com.traderx.api.ErrorHandler
import com.traderx.api.RequestService
import com.traderx.util.Injection
import com.traderx.viewmodel.UserViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class UserFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel

    private var username: String? = null
    private lateinit var usernameTextView: TextView
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString(ARG_USERNAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context?.let {
            val userViewModelFactory = Injection.provideUserViewModelFactory(it)
            userViewModel =
                ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)
        }

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        usernameTextView = view.findViewById(R.id.user_username)
        view.findViewById<Button>(R.id.follow_button)?.apply {
            setOnClickListener {
                followUser(this)
            }
        }

        disposable.add(
            userViewModel.userProfile(username ?: "")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    usernameTextView.text = it.username
                }, { context?.let { context -> ErrorHandler.handleError(it, context) } })
        )

        return view
    }

    fun followUser(button: Button) {
        if (button.isProgressActive()) {
            return
        }

        button.showProgress()

        disposable.add(
            userViewModel.followUser(username ?: "")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    button.hideProgress(R.string.pending)

                    Snackbar.make(
                        requireView(),
                        getString(R.string.follow_requested, username),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }, {
                    button.hideProgress(R.string.follow)

                    ErrorHandler.handleError(it, context as Context)
                })
        )
    }

    override fun onDetach() {
        super.onDetach()
        disposable.clear()
    }

    companion object {

        private const val ARG_USERNAME = "username"

        @JvmStatic
        fun newInstance(username: String) =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERNAME, username)
                }
            }
    }
}
