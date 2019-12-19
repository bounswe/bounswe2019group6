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
import com.traderx.util.FragmentTitleEmitters
import com.traderx.util.Helper
import com.traderx.util.Injection
import com.traderx.viewmodel.ArticleViewModel
import io.reactivex.disposables.CompositeDisposable

class ArticleCreateFragment : Fragment(), FragmentTitleEmitters {
    private lateinit var articleViewModel: ArticleViewModel

    private lateinit var header: EditText
    private lateinit var tags: EditText
    private lateinit var body: EditText
    private lateinit var warning: TextView
    private lateinit var warningLayout: ConstraintLayout

    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setFragmentTitle(context, getString(R.string.article_create))

        val articleViewModelProvider =
            Injection.provideArticleViewModelFactory(context as Context)

        articleViewModel = ViewModelProvider(this, articleViewModelProvider)
            .get(ArticleViewModel::class.java)

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_article_create, container, false)

        header = view.findViewById(R.id.header)
        tags = view.findViewById(R.id.tags)
        body = view.findViewById(R.id.body)
        warning = view.findViewById(R.id.warning)
        warningLayout = view.findViewById(R.id.warning_layout)

        view.findViewById<Button>(R.id.create_action)?.let { button ->
            button.setOnClickListener {
                createArticle(button)
            }
        }

        return view
    }

    private fun createArticle(button: Button) {

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
            articleViewModel.createArticle(
                ArticleRequest(
                    header.text.toString(),
                    parseTags(tags.text.toString()),
                    body.text.toString()
                )
            )
                .compose(Helper.applyCompletableSchedulers())
                .doFinally {
                    clearWarning()
                }
                .subscribe({
                    findNavController().navigate(
                        ArticleCreateFragmentDirections.actionNavigationArticleCreateToNavigationMyArticles(
                            null
                        )
                    )
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

}
