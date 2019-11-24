package com.traderx.ui.profile


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.traderx.R
import com.traderx.api.ErrorHandler
import com.traderx.api.response.FollowerResponse
import com.traderx.ui.search.UserSearchSkeletonRecyclerViewAdapter
import com.traderx.util.FragmentTitleEmitters
import com.traderx.util.FragmentTitleListeners
import com.traderx.util.Helper
import com.traderx.util.Injection
import com.traderx.viewmodel.AuthUserViewModel
import io.reactivex.disposables.CompositeDisposable

class PendingRequestsFragment : Fragment(), FragmentTitleEmitters {

    private lateinit var userViewModel: AuthUserViewModel
    private lateinit var recyclerView: RecyclerView
    private var isOnAction = false
    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setFragmentTitle(context as Context, getString(R.string.pending_following_requests))

        val userViewModelFactory = Injection.provideAuthUserViewModelFactory(context as Context)
        userViewModel =
            ViewModelProvider(this, userViewModelFactory).get(AuthUserViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_pending_follow_requests, container, false)

        recyclerView = root.findViewById<RecyclerView>(R.id.pending_following_requests_list).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = UserSearchSkeletonRecyclerViewAdapter(5)
        }

        disposable.add(
            userViewModel.pendingFollowRequests(context as Context)
                .compose(Helper.applySingleSchedulers<ArrayList<FollowerResponse>>())
                .subscribe({
                    recyclerView.adapter = PendingRequestRecyclerViewAdapter(it,
                        { username, onComplete -> acceptRequest(username, onComplete) },
                        { username, onComplete -> declineRequest(username, onComplete) },
                        {
                            PendingRequestsFragmentDirections.actionNavigationPendingFollowRequestsToNavigationUser(
                                it
                            )
                        }
                    )
                }, {
                    ErrorHandler.handleError(it, context as Context)
                })
        )

        return root
    }


    override fun setFragmentTitle(context: Context?, title: String?) {

        if (context is FragmentTitleListeners) {
            context.showFragmentTitle(title)
        }
    }

    private fun acceptRequest(username: String, onComplete: () -> Unit) {
        if (isOnAction) {
            return
        }

        isOnAction = true

        disposable.add(
            userViewModel.acceptFollowRequest(username)
                .compose(Helper.applyCompletableSchedulers())
                .doOnComplete {
                    onComplete()
                    isOnAction = false
                }
                .subscribe({

                    Snackbar.make(
                        requireView(),
                        getString(R.string.accept_pending_request, username),
                        Snackbar.LENGTH_SHORT
                    ).show()
                },
                    { ErrorHandler.handleError(it, context as Context) })
        )
    }

    private fun declineRequest(username: String, onComplete: () -> Unit) {
        if (isOnAction) {
            return
        }

        isOnAction = true

        disposable.add(
            userViewModel.declineFollowRequest(username)
                .compose(Helper.applyCompletableSchedulers())
                .doOnComplete {
                    onComplete()
                    isOnAction = false
                }
                .subscribe({

                    Snackbar.make(
                        requireView(),
                        getString(R.string.decline_pending_request, username),
                        Snackbar.LENGTH_SHORT
                    ).show()
                },
                    { ErrorHandler.handleError(it, context as Context) })
        )
    }
}
