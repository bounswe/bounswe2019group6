package com.traderx.ui.article


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.razir.progressbutton.hideProgress
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
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.InputStream
import kotlin.random.Random

class ArticleCreateFragment : Fragment(), FragmentTitleEmitters {
    companion object {
        const val PICK_IMAGE = 1
    }

    private lateinit var articleViewModel: ArticleViewModel

    private lateinit var header: EditText
    private lateinit var tags: EditText
    private lateinit var body: EditText
    private lateinit var createButton: Button
    private lateinit var imageAddButton: Button
    private lateinit var image: ImageView
    private var imageUri: Uri? = null
    private var imageStream: InputStream? = null
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
        image = view.findViewById(R.id.image)

        createButton = view.findViewById(R.id.create_action)
        imageAddButton = view.findViewById(R.id.image_add_button)
        createButton.setOnClickListener {
            createArticle()
        }

        imageAddButton.setOnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Pick an Image"), PICK_IMAGE)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            data?.let {
                imageUri = data.data
                imageStream = (context as Context).contentResolver.openInputStream(imageUri as Uri)
                image.setImageURI(imageUri as Uri)
            }
        }
    }

    private fun createArticle() {

        if (createButton.isProgressActive()) {
            return
        }

        var valid = false

        when {
            !ArticleValidator.validateHeader(header.text.toString()) -> setWarning(getString(R.string.article_header_error))
            !ArticleValidator.validateBody(body.text.toString()) -> setWarning(getString(R.string.article_body_error))
            !ArticleValidator.validateTags(tags.text.toString()) -> setWarning(getString(R.string.article_tags_error))
            imageStream == null || imageUri == null -> setWarning(getString(R.string.article_image_error))
            else -> valid = true
        }

        if (!valid) {
            return
        }

        imageStream?.let { stream ->
            imageUri?.let { uri ->

                val context = context as Context
                val type = context.contentResolver.getType(uri)
                val requestBody = RequestBody.create(
                    MediaType.parse(type ?: ""),
                    stream.readBytes()
                )

                val name = Random.nextInt().toString() + "." + getMime(type)
                val bodyPart = MultipartBody.Part.createFormData("file", name, requestBody)

                createButton.showProgress()

                disposable.add(
                    articleViewModel.createArticleImage(
                        bodyPart
                    )
                        .compose(Helper.applySingleSchedulers())
                        .doOnError { error ->
                            ErrorHandler.handleError(error, context)
                        }
                        .subscribe({
                            articleViewModel.createArticle(
                                ArticleRequest(
                                    header.text.toString(),
                                    parseTags(tags.text.toString()),
                                    body.text.toString(),
                                    it.url
                                )
                            )
                                .doOnError { error ->
                                    ErrorHandler.handleError(error, context)
                                }
                                .compose(Helper.applyCompletableSchedulers())
                                .subscribe({
                                    createButton.hideProgress(R.string.create)
                                    findNavController().navigate(
                                        ArticleCreateFragmentDirections.actionNavigationArticleCreateToNavigationMyArticles(
                                            null
                                        )
                                    )
                                }, { error -> ErrorHandler.handleError(error, context) })
                        }, { error -> ErrorHandler.handleError(error, context) })
                )
            }
        }


    }

    private fun getMime(type: String?): String {
        return if (type != null) {
            when (type) {
                "image/jpeg" -> "jpeg"
                "image/png" -> "png"
                "image/gif" -> "gif"
                else -> "jpeg"
            }
        } else {
            "jpeg"
        }
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
