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
import com.traderx.R
import com.traderx.api.ErrorHandler
import com.traderx.api.response.FollowerResponse
import com.traderx.ui.search.UserSearchSkeletonRecyclerViewAdapter
import com.traderx.util.FragmentTitleListeners
import com.traderx.util.Helper
import com.traderx.util.Injection
import com.traderx.viewmodel.AuthUserViewModel
import com.traderx.viewmodel.UserViewModel
import io.reactivex.disposables.CompositeDisposable

class FollowersFragment : Fragment() {
    private lateinit var username: String
    private lateinit var userViewModel: UserViewModel
    private lateinit var recyclerView: RecyclerView
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            username = it.getString(ARG_USERNAME) as String
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val userViewModelFactory = Injection.provideUserViewModelFactory(context as Context)
        userViewModel =
            ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_followers, container, false)

        recyclerView = root.findViewById<RecyclerView>(R.id.followers_list).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = UserSearchSkeletonRecyclerViewAdapter(5)
        }

        disposable.add(
            userViewModel.followers(username)
                .compose(Helper.applySingleSchedulers<List<FollowerResponse>>())
                .subscribe({
                    recyclerView.adapter = FollowersRecyclerViewAdapter(it) { username ->
                        FollowersFragmentDirections.actionNavigationFollowersToNavigationUser(
                            username
                        )
                    }
                },
                    { ErrorHandler.handleError(it, context as Context) })
        )

        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(context is FragmentTitleListeners) {
            context.showFragmentTitle(getString(R.string.followers))
        }
    }

    companion object {
        private const val ARG_USERNAME = "username"
    }
}
