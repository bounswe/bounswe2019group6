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
import com.traderx.api.response.SuccessResponse
import com.traderx.db.User
import com.traderx.enum.FollowingStatus
import com.traderx.util.Helper
import com.traderx.util.Injection
import com.traderx.viewmodel.UserViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class UserFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel

    private var username: String? = null
    private lateinit var user: User
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
            fetchUser().subscribe({
                updateUser(it)
            }, { ErrorHandler.handleError(it, context as Context) })
        )

        return root
    }

    private fun followUser(button: Button) {
        if (button.isProgressActive()) {
            return
        }

        button.showProgress()

        val status = user.followingStatus ?: ""
        val isPrivate = user.isPrivate

        val request: Single<SuccessResponse> =
            if (status == FollowingStatus.FOLLOWING.value || status == FollowingStatus.PENDING.value)
                userViewModel.unfollowUser(username ?: "")
            else
                userViewModel.followUser(username ?: "")

        disposable.add(
            request.flatMap { fetchUser().doOnSuccess { updateUser(it) } }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    button.hideProgress(getButtonTextId(it.followingStatus ?: ""))

                    Snackbar.make(
                        requireView(),
                        getActionMessage(status, isPrivate),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }, {
                    button.hideProgress(getButtonTextId(user.followingStatus ?: ""))

                    ErrorHandler.handleError(it, context as Context)
                })
        )
    }

    override fun onDetach() {
        super.onDetach()
        disposable.clear()
    }

    private fun fetchUser(): Single<User> {
        return userViewModel.userProfile(username ?: "")
            .compose(Helper.applySchedulers<User>())
    }

    private fun updateUser(user: User) {
        this.user = user
        userName.text = user.username
        role.text = user.localizedRole(context as Context)
        profilePrivate.text = user.localizedIsPrivate(context as Context)
        followerCount.text = user.followersCount.toString()
        followingCount.text = user.followingsCount.toString()
        followButton.text = user.localizedFollowingStatus(context as Context)
        val status = user.followingStatus ?: ""
        followButton.text = getString(getButtonTextId(status))
    }

    private fun getButtonTextId(status: String): Int {
        return when(status) {
            FollowingStatus.PENDING.value -> R.string.cancel_request
            FollowingStatus.FOLLOWING.value -> R.string.unfollow
            else -> R.string.follow
        }
    }

    private fun getActionMessage(status: String, isPrivate: Boolean): String {

        var message = when {
            status == FollowingStatus.PENDING.value -> R.string.request_canceled
            status == FollowingStatus.FOLLOWING.value -> R.string.unfollowed
            isPrivate -> R.string.follow_requested
            else -> R.string.follow_success
        }

        return getString(message, username)
    }

    companion object {

        private const val ARG_USERNAME = "username"
    }
}
