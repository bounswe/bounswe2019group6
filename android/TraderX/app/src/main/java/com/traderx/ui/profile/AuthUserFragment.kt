package com.traderx.ui.profile


import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.traderx.MainActivity
import com.traderx.R
import com.traderx.api.ApiService
import com.traderx.api.ErrorHandler
import com.traderx.api.RequestService
import com.traderx.db.User
import com.traderx.enum.Role
import com.traderx.ui.auth.signup.SignUpValidator
import com.traderx.util.Helper
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
    private lateinit var becomeTraderLayout: LinearLayout
    private lateinit var requestService: RequestService

    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_auth_user, container, false)

        userName = root.findViewById(R.id.profile_username)
        email = root.findViewById(R.id.profile_email)
        role = root.findViewById(R.id.profile_role)
        profilePrivate = root.findViewById(R.id.profile_private)
        followerCount = root.findViewById(R.id.profile_follower)
        followingCount = root.findViewById(R.id.profile_following)
        becomeTraderLayout = root.findViewById(R.id.become_trader_action)
        becomeTraderLayout.setOnClickListener {
            becomeTrader(inflater)
        }

        root.findViewById<LinearLayout>(R.id.followers_list_action)?.let {
            it.setOnClickListener {
                if (isClickable()) {
                    findNavController().navigate(
                        AuthUserFragmentDirections.actionNavigationAuthUserToNavigationFollowers(
                            user.username
                        )
                    )
                }
            }
        }

        root.findViewById<LinearLayout>(R.id.followings_list_action)?.let {
            it.setOnClickListener {
                if (isClickable()) {
                    findNavController().navigate(
                        AuthUserFragmentDirections.actionNavigationAuthUserToNavigationFollowings(
                            user.username
                        )
                    )
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

                    if (user.role == Role.ROLE_BASIC.value) {
                        becomeTraderLayout.visibility = View.VISIBLE
                    }

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

    private fun becomeTrader(inflater: LayoutInflater) {

        val builder = AlertDialog.Builder(context as Context)

        val view = inflater.inflate(R.layout.dialog_become_trader, null)

        builder.setView(view)
        val warningLayout = view.findViewById<LinearLayout>(R.id.warning_layout)
        val warning = view.findViewById<TextView>(R.id.warning)
        val progress = view.findViewById<ProgressBar>(R.id.progress)
        val iban = view.findViewById<EditText>(R.id.iban)
        val okButton = view.findViewById<Button>(R.id.ok_action)

        builder.setNegativeButton(R.string.cancel) { _, _ -> }

        val alertDialog = builder.create()
        alertDialog.show()

        okButton.setOnClickListener {
            if (!SignUpValidator.validateIban(iban.text.toString())) {
                warningLayout.visibility = View.VISIBLE
                warning.text = getString(R.string.trader_iban_not_valid)
            } else {
                warningLayout.visibility = View.GONE

                progress.visibility = View.VISIBLE

                disposable.add(
                    authUserViewModel.becomeTrader(iban.text.toString())
                        .compose(Helper.applyCompletableSchedulers())
                        .subscribe({
                            alertDialog.dismiss()
                        }, {
                            progress.visibility = View.GONE
                            ErrorHandler.handleError(it, context as Context)
                        })
                )
            }
        }
    }
}
