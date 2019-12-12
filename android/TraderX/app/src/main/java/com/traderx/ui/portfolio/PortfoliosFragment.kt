package com.traderx.ui.portfolio


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
import com.traderx.api.response.PortfolioResponse
import com.traderx.ui.profile.FollowersFragmentDirections
import com.traderx.ui.search.UserSearchSkeletonRecyclerViewAdapter
import com.traderx.util.FragmentTitleEmitters
import com.traderx.util.FragmentTitleListeners
import com.traderx.util.Helper
import com.traderx.util.Injection
import com.traderx.viewmodel.AuthUserViewModel
import com.traderx.viewmodel.PortfolioViewModel
import io.reactivex.disposables.CompositeDisposable

class PortfoliosFragment : Fragment(), FragmentTitleEmitters {

    private lateinit var userViewModel: AuthUserViewModel
    private lateinit var recyclerView: RecyclerView
    private val disposable = CompositeDisposable()
    private lateinit var portfolioViewModel: PortfolioViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setFragmentTitle(context as Context, "My Portfolio")

        val userViewModelFactory = Injection.provideAuthUserViewModelFactory(context as Context)

        userViewModel =
            ViewModelProvider(this, userViewModelFactory).get(AuthUserViewModel::class.java)
        val portfolioViewModelFactory =
            Injection.provideEquipmentViewModelFactory(context as Context)


        portfolioViewModel = ViewModelProvider(this, portfolioViewModelFactory)
            .get(PortfolioViewModel::class.java)


        val root = inflater.inflate(R.layout.fragment_portfolio, container, false)

        recyclerView = root.findViewById<RecyclerView>(R.id.portfolio_list).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = UserSearchSkeletonRecyclerViewAdapter(5)
        }

        disposable.add(
            portfolioViewModel.getPortfolios()
                .compose(Helper.applySingleSchedulers<ArrayList<PortfolioResponse>>())
                .subscribe({
                    recyclerView.adapter =
                        PortfolioRecyclerViewAdapter(
                            it,
                            { name, onComplete -> deletePortfolio(name, onComplete) },
                            { username ->
                                FollowersFragmentDirections.actionNavigationFollowersToNavigationUser(
                                    username
                                )
                            }
                        )
                }, {})
        )

        return root
    }


    private fun deletePortfolio(name: String, onComplete: () -> Unit) {

        disposable.add(
            portfolioViewModel.deletePortfolio(name)
                .compose(Helper.applyCompletableSchedulers())
                .doFinally {
                    onComplete()
                }
                .subscribe({
                    Snackbar.make(
                        requireView(),
                        getString(R.string.delete_portfolio),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }, {
                    ErrorHandler.handleError(it, context as Context)
                })
        )
    }


    override fun setFragmentTitle(context: Context?, title: String?) {

        if (context is FragmentTitleListeners) {
            context.showFragmentTitle(title)
        }
    }


}
