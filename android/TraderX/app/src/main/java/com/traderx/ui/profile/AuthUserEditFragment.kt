package com.traderx.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.isProgressActive
import com.github.razir.progressbutton.showProgress
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.traderx.R
import com.traderx.api.ErrorHandler
import com.traderx.db.User
import com.traderx.util.Helper
import com.traderx.util.Injection
import com.traderx.viewmodel.AuthUserViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AuthUserEditFragment : Fragment() {

    private lateinit var authUserViewModel: AuthUserViewModel

    private lateinit var user: User
    private lateinit var username: EditText
    private lateinit var profilePrivate: Switch

    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_auth_user_edit, container, false)

        username = view.findViewById(R.id.edit_username)
        profilePrivate = view.findViewById(R.id.edit_profile_private)

        view.findViewById<Button>(R.id.edit_save)?.apply {
            setOnClickListener {
                updateProfile(this)
            }
        }

        val authUserViewModelFactory = Injection.provideAuthUserViewModelFactory(view.context)
        authUserViewModel =
            ViewModelProvider(this, authUserViewModelFactory).get(AuthUserViewModel::class.java)

        disposable.add(
            authUserViewModel.user(context as Context)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    user = it
                    username.setText(it.username)
                    profilePrivate.setChecked(it.isPrivate)
                }, { ErrorHandler.handleError(it, context as Context) })
        )

        return view
    }

    fun updateProfile(button: Button) {

        if(button.isProgressActive()) {
            return
        }

        button.showProgress()

        disposable.add(
            authUserViewModel.updateUser(profilePrivate.isChecked)
                .compose(Helper.applyCompletableSchedulers())
                .doOnComplete {
                    button.hideProgress(R.string.save)
                }
                .subscribe({
                    Snackbar.make(requireView(), "Updated profile", Snackbar.LENGTH_SHORT).show()

                }, { ErrorHandler.handleError(it, context as Context) })

        )

    }
}
