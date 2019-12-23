package com.traderx.ui.article


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.traderx.R
import com.traderx.api.ErrorHandler
import com.traderx.api.request.AnnotationRequest
import com.traderx.api.response.AnnotationResponse
import com.traderx.api.response.CommentResponse
import com.traderx.db.Article
import com.traderx.selectabletextview.SelectableTextView
import com.traderx.type.AnnotationType
import com.traderx.ui.comment.CommentFragment
import com.traderx.util.FragmentTitleEmitters
import com.traderx.util.Helper
import com.traderx.util.Injection
import com.traderx.viewmodel.ArticleViewModel
import com.traderx.viewmodel.AuthUserViewModel
import io.reactivex.disposables.CompositeDisposable


class ArticleFragment : Fragment(), FragmentTitleEmitters {
    companion object {
        private const val ARTICLE_ID = "id"
        private const val DEFAULT_SELECTION_LEN = 5

    }

    private lateinit var articleViewModel: ArticleViewModel
    private lateinit var authUserViewModel: AuthUserViewModel

    private var articleId: Int = 0
    private lateinit var image: ImageView
    private lateinit var article: Article
    private lateinit var annotations: ArrayList<AnnotationResponse>
    private lateinit var authUsername: String
    private lateinit var header: TextView
    private lateinit var body: SelectableTextView
    private lateinit var tags: TextView
    private lateinit var username: TextView
    private lateinit var createdAt: TextView
    private lateinit var editButton: Button

    private var touchX = 0
    private var touchY = 0

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
        setFragmentTitle(context, getString(R.string.article))

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
        image = root.findViewById(R.id.image)
        createdAt = root.findViewById(R.id.created_at)

        selectionAttach()

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

        refreshAnnotations()

        return root
    }

    private fun showAnnotations() {
        if (::article.isInitialized && ::annotations.isInitialized) {
            val spannableString = SpannableString(article.body)

            for (i in 0..(annotations.size - 1)) {
                val annotation = annotations[i]

                if (annotation.target.selector.start >= annotation.target.selector.end || annotation.target.selector.end >= article.body.length) {
                    continue
                }

                spannableString.setSpan(
                    BackgroundColorSpan(Color.argb(120, 50, 50, 220)),
                    annotation.target.selector.start,
                    annotation.target.selector.start + 1,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )
                spannableString.setSpan(
                    object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            showAnnotationsListModal(i)
                        }
                    },
                    annotation.target.selector.start,
                    annotation.target.selector.end,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )
            }

            body.setText(spannableString)
            body.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    override fun onDetach() {
        super.onDetach()

        body.hideCursor()
    }

    private fun selectionAttach() {
        body.setDefaultSelectionColor(0x40FF00FF)

        body.setOnLongClickListener {
            setFragmentActionButton(context, getString(R.string.annotate)) {
                val articleAnnotateModal = ArticleAnnotateCreateModal({ annotation ->
                    articleViewModel.createAnnotation(
                        AnnotationRequest(
                            articleId,
                            annotation,
                            AnnotationType.TEXT.value,
                            AnnotationType.TEXT.value,
                            body.cursorSelection.start,
                            body.cursorSelection.end
                        )
                    )
                }, {
                    hideFragmentActionButton(context as Context)
                    refreshAnnotations()
                    body.hideCursor()
                }, requireView())

                articleAnnotateModal.show(
                    fragmentManager as FragmentManager,
                    ArticleAnnotateCreateModal.TAG
                )
            }

            showSelectionCursors(touchX, touchY)

            true
        }

        body.setOnClickListener {
            body.hideCursor()
            hideFragmentActionButton(context)
        }

        body.setOnTouchListener { _, event ->
            touchX = event.x.toInt()
            touchY = event.y.toInt()
            false
        }
    }

    private fun showSelectionCursors(x: Int, y: Int) {
        val start = body.getPreciseOffset(x, y)

        if (start > -1) {
            var end = start + DEFAULT_SELECTION_LEN
            if (end >= body.text.length) {
                end = body.text.length - 1
            }
            body.showSelectionControls(start, end)
        }
    }

    private fun showAnnotationsListModal(index: Int) {
        val annotationList = ArrayList<AnnotationResponse>()
        val currentAnnotationStart = annotations[index].target.selector.start
        val currentAnnotationEnd = annotations[index].target.selector.end

        annotationList.add(annotations[index])
        for (i in 0..(annotations.size - 1)) {
            if (i == index) {
                continue
            }

            val annotation = annotations[i]

            if ((annotation.target.selector.start >= currentAnnotationStart - 2 &&
                        annotation.target.selector.start <= currentAnnotationEnd + 2) ||
                (annotation.target.selector.end >= currentAnnotationStart - 2 &&
                        annotation.target.selector.end <= currentAnnotationEnd + 2)
            ) {
                annotationList.add(annotation)
            }
        }

        val articleAnnotateListModal =
            ArticleAnnotateListModal(authUsername, annotationList) { id ->
                val index = annotations.find { it.id == id }
                if (index != null) annotations.remove(index)
                showAnnotations()
                articleViewModel.deleteAnnotation(id)
            }

        articleAnnotateListModal.show(
            fragmentManager as FragmentManager,
            ArticleAnnotateListModal.TAG
        )
    }

    private fun refreshAnnotations() {
        disposable.add(
            articleViewModel.getAnnotations(articleId)
                .compose(Helper.applySingleSchedulers())
                .subscribe(
                    {
                        annotations = it
                        showAnnotations()
                    },
                    { ErrorHandler.handleError(it, context as Context) })
        )
    }

    private fun checkEditable() {
        if (::authUsername.isInitialized && ::article.isInitialized) {
            editButton.visibility =
                if (authUsername == article.username) View.VISIBLE else View.GONE
        }
    }

    private fun updateView() {
        header.text = article.header
        body.text = article.body
        createdAt.text = article.createdAt
        username.text = article.username
        tags.text = article.tags.joinToString { it }
        username.text = article.username
        showAnnotations()
        article.imageUrl?.let { DownloadImageTask(image).execute(it) }
    }

    private class DownloadImageTask(private var bmImage: ImageView) :
        AsyncTask<String, Void, Bitmap>() {

        override fun doInBackground(vararg urls: String): Bitmap? {
            val urldisplay = urls[0]
            var mIcon11: Bitmap? = null
            try {
                val `in` = java.net.URL(urldisplay).openStream()
                mIcon11 = BitmapFactory.decodeStream(`in`)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return mIcon11
        }

        override fun onPostExecute(result: Bitmap) {
            bmImage.setImageBitmap(result)
        }
    }
}


