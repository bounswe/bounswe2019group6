package com.traderx.ui.article


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.traderx.R
import com.traderx.api.ErrorHandler
import com.traderx.ui.search.UserSearchSkeletonRecyclerViewAdapter
import com.traderx.util.FragmentTitleEmitters
import com.traderx.util.Helper
import com.traderx.util.Injection
import com.traderx.viewmodel.ArticleViewModel
import com.traderx.viewmodel.AuthUserViewModel
import io.reactivex.disposables.CompositeDisposable

/**
 * A simple [Fragment] subclass.
 */
class MyArticlesFragment : Fragment(), FragmentTitleEmitters {
    private lateinit var articleViewModel: ArticleViewModel
    private lateinit var authUserViewModel: AuthUserViewModel

    private var username: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: LinearLayoutManager

    private val disposable = CompositeDisposable()

    private var isDeleting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            username = it.getString(ARG_USERNAME)
        }

        val authUserViewModelFactory = Injection.provideAuthUserViewModelFactory(context as Context)
        authUserViewModel =
            ViewModelProvider(this, authUserViewModelFactory).get(AuthUserViewModel::class.java)

        val articleViewModelProvider =
            Injection.provideArticleViewModelFactory(context as Context)
        articleViewModel =
            ViewModelProvider(this, articleViewModelProvider).get(ArticleViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setFragmentTitle(context, getString(R.string.title_my_articles))

        val root = inflater.inflate(R.layout.fragment_articles, container, false)

        viewManager = LinearLayoutManager(context)
        recyclerView = root.findViewById<RecyclerView>(R.id.article_list).apply {
            layoutManager = viewManager
            adapter = UserSearchSkeletonRecyclerViewAdapter(5)
        }

        root.findViewById<FloatingActionButton>(R.id.article_create_action)?.let {
            it.setOnClickListener {
                findNavController().navigate(MyArticlesFragmentDirections.actionNavigationMyArticlesToNavigationArticleCreate())
            }
        }

        username?.let {
            getArticles(it)
        }

        if (username == null) {
            disposable.add(
                authUserViewModel.userOrNew(context as Context)
                    .compose(Helper.applySingleSchedulers())
                    .subscribe({
                        username = it.username
                        getArticles(it.username)
                    }, { ErrorHandler.handleError(it, context as Context) })
            )
        }

        return root
    }

    private fun getArticles(username: String) {
        disposable.add(
            articleViewModel.getUserArticles(username)
                .compose(Helper.applyFlowableSchedulers())
                .subscribe({
                    recyclerView.adapter = ArticleRecyclerViewAdapter(it, { id ->
                        MyArticlesFragmentDirections.actionNavigationMyArticlesToNavigationArticle(
                            id
                        )
                    }, fragmentManager as FragmentManager) { id, doOnSuccess ->
                        if (!isDeleting) {
                            isDeleting = true
                            disposable.add(
                                articleViewModel.deleteArticle(id)
                                    .compose(Helper.applyCompletableSchedulers())
                                    .doOnComplete(doOnSuccess)
                                    .subscribe({
                                        Snackbar.make(
                                            requireView(),
                                            getString(R.string.article_delete_success),
                                            Snackbar.LENGTH_SHORT
                                        ).show()

                                        isDeleting = false
                                    }, { ErrorHandler.handleError(it, context as Context) })
                            )
                        }
                    }
                }, {
                    ErrorHandler.handleError(it, context as Context)
                })
        )
    }

    companion object {
        const val ARG_USERNAME = "username"
    }
}
