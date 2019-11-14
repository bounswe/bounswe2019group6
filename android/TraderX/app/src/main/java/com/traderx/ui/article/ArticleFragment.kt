package com.traderx.ui.article


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.traderx.R
import com.traderx.db.Article
import com.traderx.util.Injection
import com.traderx.viewmodel.ArticleViewModel

class ArticleFragment : Fragment() {
    companion object {
        private const val ARTICLE_ID = "articleId"
    }

    private lateinit var articleViewModel: ArticleViewModel

    private var articleId: Int? = null
    private var article: Article? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            articleId = it.getInt(ARTICLE_ID)
        }

        val articleViewModelProvider = Injection.provideArticleUserViewModelFactory(context as Context)
        articleViewModel = ViewModelProvider(this, articleViewModelProvider).get(ArticleViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_article, container, false)

        return root
    }
}
