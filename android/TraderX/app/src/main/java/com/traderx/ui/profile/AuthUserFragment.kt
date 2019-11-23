package com.traderx.ui.profile


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.isProgressActive
import com.github.razir.progressbutton.showProgress
import com.traderx.MainActivity
import com.traderx.R
import com.traderx.api.ApiService
import com.traderx.api.ErrorHandler
import com.traderx.api.RequestService
import com.traderx.db.User
import com.traderx.util.Injection
import com.traderx.util.TokenUtility
import com.traderx.viewmodel.AuthUserViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AuthUserFragment : Fragment() {

    private lateinit var authUserViewModel: AuthUserViewModel

    private lateinit var user: User
    private lateinit var userName: TextView
    private lateinit var email: TextView
    private lateinit var role: TextView
    private lateinit var followerCount: TextView
    private lateinit var followingCount: TextView
    private lateinit var profilePrivate: TextView
    private lateinit var requestService: RequestService

    private val disposable = CompositeDisposable()

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
        followerCount = root.findViewById(R.id.profile_follower)
        followingCount = root.findViewById(R.id.profile_following)

        root.findViewById<LinearLayout>(R.id.followers_list_action)?.let {
            it.setOnClickListener {
                if(isClickable()) {
                    findNavController().navigate(AuthUserFragmentDirections.actionNavigationAuthUserToNavigationFollowers(user.username))
                }
            }
        }

        root.findViewById<LinearLayout>(R.id.followings_list_action)?.let {
            it.setOnClickListener {
                if(isClickable()) {
                    findNavController().navigate(AuthUserFragmentDirections.actionNavigationAuthUserToNavigationFollowings(user.username))
                }
            }
        }

        root.findViewById<LinearLayout>(R.id.pending_follow_requests_action)?.let {
            it.setOnClickListener {
                findNavController().navigate(AuthUserFragmentDirections.actionNavigationAuthUserToNavigationPendingFollowRequests())
            }
        }

        root.findViewById<ImageView>(R.id.action_menu)?.let { imageView ->
            imageView.setOnClickListener {
                showMenu(it as View)
            }
        }

        val authUserViewModelFactory = Injection.provideAuthUserViewModelFactory(root.context)
        authUserViewModel =
            ViewModelProvider(this, authUserViewModelFactory).get(AuthUserViewModel::class.java)

        requestService = ApiService.getInstance(context)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val context: Context = this.context as Context

        val activity = this.activity as FragmentActivity

        disposable.add(
            authUserViewModel.user(context)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    user = it
                    userName.text = it.username
                    email.text = it.email
                    role.text = it.localizedRole(context)
                    profilePrivate.text = it.localizedIsPrivate(context)
                    followerCount.text = it.followersCount.toString()
                    followingCount.text = it.followingsCount.toString()
                }, {
                    ErrorHandler.handleError(it, activity)
                })
        )
    }

    override fun onStop() {
        super.onStop()

        disposable.clear()
    }

    private fun isClickable(): Boolean {
        return ::user.isInitialized
    }

    private fun showMenu(anchor: View) {
        val popupMenu = PopupMenu(context, anchor)

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.signout_action -> logout()
                R.id.edit_profile_action -> findNavController().navigate(AuthUserFragmentDirections.actionNavigationAuthUserToNavigationUserEdit())
                else -> false
            }

            true
        }

        popupMenu.menuInflater.inflate(R.menu.user_profile_menu, popupMenu.menu)
        popupMenu.show()
    }

    private fun logout() {
        val single = authUserViewModel.deleteUser().andThen(requestService.signout())

        disposable.add(
            single
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    context?.apply { TokenUtility.clearToken(this) }
                    startActivity(Intent(context, MainActivity::class.java))
                }, {
                    context?.apply { ErrorHandler.handleError(it, this) }
                })
        )
    }

}
