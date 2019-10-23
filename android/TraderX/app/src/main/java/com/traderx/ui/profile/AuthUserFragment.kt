package com.traderx.ui.profile


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.traderx.R
import com.traderx.adapter.UserAdapter
import com.traderx.api.ErrorHandler
import com.traderx.util.Injection
import com.traderx.viewmodel.UserViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AuthUserFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel

    private lateinit var userName: TextView
    private lateinit var email: TextView
    private lateinit var role: TextView
    private lateinit var profilePrivate: TextView

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val root = inflater.inflate(R.layout.fragment_auth_user, container, false)

        userName = root.findViewById(R.id.profile_username)
        email = root.findViewById(R.id.profile_email)
        role = root.findViewById(R.id.profile_role)
        profilePrivate = root.findViewById(R.id.profile_private)

        val userViewModelFactory = Injection.provideUserViewModelFactory(root.context)

        userViewModel = ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val context: Context = this.context as Context

        val activity = this.activity as FragmentActivity

        disposable.add(
            userViewModel.user(context)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    userName.text = it.username
                    email.text = it.email
                    role.text = getString(UserAdapter.adaptRole(it.role))
                    profilePrivate.text =
                        if (it.isPrivate) getString(R.string.general_true) else getString(R.string.general_false)
                }, {
                    ErrorHandler.handleError(it, activity)
                })
        )
    }
}
