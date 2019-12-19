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
import com.traderx.R
import com.traderx.api.ErrorHandler
import com.traderx.ui.search.UserSearchSkeletonRecyclerViewAdapter
import com.traderx.util.FragmentTitleEmitters
import com.traderx.util.Helper
import com.traderx.util.Injection
import com.traderx.viewmodel.ArticleViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ArticlesFragment : Fragment(), FragmentTitleEmitters {
    private lateinit var articleViewModel: ArticleViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: LinearLayoutManager

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val articleViewModelProvider =
            Injection.provideArticleViewModelFactory(context as Context)
        articleViewModel =
            ViewModelProvider(this, articleViewModelProvider).get(ArticleViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setFragmentTitle(context, getString(R.string.title_articles))

        val root = inflater.inflate(R.layout.fragment_articles, container, false)

        viewManager = LinearLayoutManager(context)
        recyclerView = root.findViewById<RecyclerView>(R.id.article_list).apply {
            layoutManager = viewManager
            adapter = UserSearchSkeletonRecyclerViewAdapter(5)
        }

        root.findViewById<FloatingActionButton>(R.id.article_create_action)?.let {
            it.setOnClickListener {
                findNavController().navigate(ArticlesFragmentDirections.actionNavigationArticlesToNavigationArticleCreate())
            }
        }

        disposable.add(
            articleViewModel.getArticles()
                .compose(Helper.applyFlowableSchedulers())
                .subscribe({
                    recyclerView.adapter = ArticleRecyclerViewAdapter(it, {id ->
                        ArticlesFragmentDirections.actionNavigationArticlesToNavigationArticle(id)
                    }, fragmentManager as FragmentManager, null)
                }, {
                    ErrorHandler.handleError(it, context as Context)
                })
        )

        return root
    }
}
