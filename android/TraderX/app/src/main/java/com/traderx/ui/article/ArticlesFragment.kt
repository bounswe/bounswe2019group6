package com.traderx.ui.article

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
import com.traderx.util.Injection
import com.traderx.viewmodel.ArticleViewModel
import io.reactivex.disposables.CompositeDisposable

class ArticlesFragment : Fragment() {
    private lateinit var articleViewModel: ArticleViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: LinearLayoutManager

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val articleViewModelProvider =
            Injection.provideArticleUserViewModelFactory(context as Context)
        articleViewModel =
            ViewModelProvider(this, articleViewModelProvider).get(ArticleViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_articles, container, false)

        viewManager = LinearLayoutManager(context)
        recyclerView = root.findViewById<RecyclerView>(R.id.article_list).apply {
            layoutManager = viewManager
        }

//        disposable.add(
//            articleViewModel.getArticles()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe({
//                    recyclerView.swapAdapter(ArticleRecyclerViewAdapter(it), false)
//                }, {
//                    ErrorHandler.handleError(it, context as Context)
//                })
//        )

        return root
    }
}
