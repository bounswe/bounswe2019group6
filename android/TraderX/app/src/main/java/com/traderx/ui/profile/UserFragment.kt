package com.traderx.ui.profile

import android.content.Context
import android.os.Bundle
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
import com.traderx.api.ErrorHandler
import com.traderx.enum.FollowingStatus
import com.traderx.util.Injection
import com.traderx.viewmodel.UserViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class UserFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel

    private var username: String? = null
    private lateinit var userName: TextView
    private lateinit var role: TextView
    private lateinit var followerCount: TextView
    private lateinit var followingCount: TextView
    private lateinit var profilePrivate: TextView
    private lateinit var followButton: Button
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
        val root = inflater.inflate(R.layout.fragment_user, container, false)

        userName = root.findViewById(R.id.profile_username)
        role = root.findViewById(R.id.profile_role)
        profilePrivate = root.findViewById(R.id.profile_private)
        followerCount = root.findViewById(R.id.profile_follower)
        followingCount = root.findViewById(R.id.profile_following)
        followButton = root.findViewById<Button>(R.id.follow_button).also { button ->
            button.setOnClickListener {
                followUser(button)
            }
        }

        disposable.add(
            userViewModel.userProfile(username ?: "")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    userName.text = it.username
                    role.text = it.localizedRole(context as Context)
                    profilePrivate.text = it.localizedIsPrivate(context as Context)
                    followerCount.text = it.followersCount.toString()
                    followingCount.text = it.followingsCount.toString()
                    followButton.text = it.localizedFollowingStatus(context as Context)
                    followButton.setEnabled(it.followingStatus != FollowingStatus.PENDING.value)

                }, { context?.let { context -> ErrorHandler.handleError(it, context) } })
        )

        return root
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
