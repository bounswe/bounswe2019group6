package com.traderx.ui.article


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.traderx.R
import com.traderx.api.ErrorHandler
import com.traderx.api.response.CommentResponse
import com.traderx.db.Article
import com.traderx.ui.comment.CommentFragment
import com.traderx.util.Helper
import com.traderx.util.Injection
import com.traderx.viewmodel.ArticleViewModel
import com.traderx.viewmodel.AuthUserViewModel
import io.reactivex.disposables.CompositeDisposable

class ArticleFragment : Fragment() {
    companion object {
        private const val ARTICLE_ID = "id"
    }

    private lateinit var articleViewModel: ArticleViewModel
    private lateinit var authUserViewModel: AuthUserViewModel

    private var articleId: Int = 0
    private lateinit var authUsername: String
    private lateinit var article: Article
    private lateinit var header: TextView
    private lateinit var body: TextView
    private lateinit var tags: TextView
    private lateinit var username: TextView
    private lateinit var createdAt: TextView
    private lateinit var editButton: Button

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            articleId = it.getInt(ARTICLE_ID)
        }

        val articleViewModelProvider =
            Injection.provideArticleViewModelFactory(context as Context)

        articleViewModel = ViewModelProvider(this, articleViewModelProvider)
            .get(ArticleViewModel::class.java)

        val authUserViewModelFactory = Injection.provideAuthUserViewModelFactory(context as Context)
        authUserViewModel =
            ViewModelProvider(this, authUserViewModelFactory).get(AuthUserViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_article, container, false)

        disposable.add(
            articleViewModel.getArticle(articleId)
                .compose(Helper.applySingleSchedulers())
                .subscribe({
                    article = it
                    updateView()
                    checkEditable()
                }, { ErrorHandler.handleError(it, context as Context) })
        )

        header = root.findViewById(R.id.header)
        body = root.findViewById(R.id.body)
        tags = root.findViewById(R.id.tags)
        username = root.findViewById(R.id.username)
        createdAt = root.findViewById(R.id.created_at)

        editButton = root.findViewById<Button>(R.id.article_edit_action).also {
            it.setOnClickListener {
                findNavController().navigate(
                    ArticleFragmentDirections.actionNavigationArticleToNavigationArticleEdit(
                        articleId
                    )
                )
            }
        }

        disposable.add(
            authUserViewModel.userOrNew(context as Context)
                .compose(Helper.applySingleSchedulers())
                .subscribe({
                    authUsername = it.username
                    checkEditable()
                }, {})
        )

        root.findViewById<FrameLayout>(R.id.comments)?.let {
            val commentFragment = CommentFragment(
                articleViewModel.getComments(articleId)
                    .compose(Helper.applySingleSchedulers<ArrayList<CommentResponse>>()),
                { id ->
                    articleViewModel.deleteComment(id)
                },
                { id, message ->
                    articleViewModel.editComment(id, message)
                },
                { message ->
                    articleViewModel.createComment(articleId, message)
                },
                { id, vote ->
                    articleViewModel.voteComment(id, vote)
                },
                { id ->
                    articleViewModel.revokeComment(id)
                }
            )

            val fragmentTransaction = fragmentManager?.beginTransaction()

            fragmentTransaction?.add(it.id, commentFragment, CommentFragment.TAG)
            fragmentTransaction?.commit()
        }

        return root
    }

    private fun checkEditable() {
        if(::authUsername.isInitialized && ::article.isInitialized) {
            editButton.visibility = if(authUsername == article.username) View.VISIBLE else View.GONE
        }
    }

    private fun updateView() {
        header.text = article.header
        body.text = article.body
        createdAt.text = article.createdAt
        username.text = article.username
        tags.text = article.tags.joinToString { it }
        username.text = article.username
    }
}
