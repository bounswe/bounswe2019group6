package com.traderx.ui.article

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.razir.progressbutton.isProgressActive
import com.github.razir.progressbutton.showProgress
import com.traderx.R
import com.traderx.api.ErrorHandler
import com.traderx.api.request.ArticleRequest
import com.traderx.db.Article
import com.traderx.util.FragmentTitleEmitters
import com.traderx.util.Helper
import com.traderx.util.Injection
import com.traderx.viewmodel.ArticleViewModel
import io.reactivex.disposables.CompositeDisposable

class ArticleEditFragment : Fragment(), FragmentTitleEmitters {
    private var articleId: Int = 0

    private lateinit var articleViewModel: ArticleViewModel

    private lateinit var article: Article
    private lateinit var header: EditText
    private lateinit var tags: EditText
    private lateinit var body: EditText
    private lateinit var warning: TextView
    private lateinit var warningLayout: ConstraintLayout

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            articleId = it.getInt(ARTICLE_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_article_create, container, false)

        setFragmentTitle(context, getString(R.string.article_create))

        val articleViewModelProvider =
            Injection.provideArticleViewModelFactory(context as Context)

        articleViewModel = ViewModelProvider(this, articleViewModelProvider)
            .get(ArticleViewModel::class.java)

        header = view.findViewById(R.id.header)
        tags = view.findViewById(R.id.tags)
        body = view.findViewById(R.id.body)
        warning = view.findViewById(R.id.warning)
        warningLayout = view.findViewById(R.id.warning_layout)

        disposable.add(
            articleViewModel.getArticle(articleId)
                .compose(Helper.applySingleSchedulers())
                .subscribe({
                    article = it
                    header.setText(article.header)
                    body.setText(article.body)
                    tags.setText(article.tags.joinToString { it })
                }, { ErrorHandler.handleError(it, context as Context) })
        )

        view.findViewById<Button>(R.id.create_action)?.let { button ->
            button.text = getString(R.string.edit)
            button.setOnClickListener {
                if (::article.isInitialized) {
                    editArticle(button)
                }
            }
        }

        return view
    }

    private fun editArticle(button: Button) {
        if (button.isProgressActive()) {
            return
        }

        var valid = false

        when {
            !ArticleValidator.validateHeader(header.text.toString()) -> setWarning(getString(R.string.article_header_error))
            !ArticleValidator.validateBody(body.text.toString()) -> setWarning(getString(R.string.article_body_error))
            !ArticleValidator.validateTags(tags.text.toString()) -> setWarning(getString(R.string.article_tags_error))
            else -> valid = true
        }

        if (!valid) {
            return
        }

        button.showProgress()

        disposable.add(
            articleViewModel.editArticle(
                articleId,
                ArticleRequest(
                    header.text.toString(),
                    parseTags(tags.text.toString()),
                    body.text.toString(),
                    article.url
                )
            )
                .compose(Helper.applyCompletableSchedulers())
                .doFinally {
                    clearWarning()
                }
                .subscribe({
                    findNavController().navigate(ArticleEditFragmentDirections.actionNavigationArticleEditToNavigationMyArticles(null))
                }, { ErrorHandler.handleError(it, context as Context) })
        )
    }

    private fun parseTags(tags: String): List<String> {
        return tags.split(",").map { it.trim() }
    }

    private fun setWarning(warning: String) {
        this.warning.text = warning
        warningLayout.visibility = View.VISIBLE
    }

    private fun clearWarning() {
        warning.text = ""
        warningLayout.visibility = View.GONE
    }

    companion object {
        const val ARTICLE_ID = "id"
    }
}
