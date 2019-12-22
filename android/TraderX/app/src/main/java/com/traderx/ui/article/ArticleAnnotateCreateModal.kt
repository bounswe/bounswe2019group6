package com.traderx.ui.article

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.github.razir.progressbutton.isProgressActive
import com.github.razir.progressbutton.showProgress
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.traderx.R
import com.traderx.api.ErrorHandler
import com.traderx.util.Helper
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

class ArticleAnnotateCreateModal(
    val onCreate: (annotation: String) -> Completable,
    val onCreateSucces: () -> Unit,
    val parentView: View
) : BottomSheetDialogFragment() {

    private lateinit var annotation: EditText
    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.modal_annotation_create, container, false)

        annotation = view.findViewById(R.id.annotation)

        view.findViewById<Button>(R.id.create)?.let { button ->
            button.setOnClickListener {
                if (AnnotationValidator.validateText(annotation.text.toString())) {
                    if (!button.isProgressActive()) {
                        button.showProgress()
                        disposable.add(
                            onCreate(annotation.text.toString())
                                .compose(Helper.applyCompletableSchedulers())
                                .doOnComplete(onCreateSucces)
                                .subscribe({
                                    dismiss()

                                    Snackbar.make(
                                        parentView,
                                        getString(R.string.annotation_create_success),
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                }, { ErrorHandler.handleError(it, context as Context) })
                        )
                    }
                }
            }
        }

        view.findViewById<Button>(R.id.cancel)?.let {
            it.setOnClickListener {
                dismiss()
            }
        }

        return view
    }

    companion object {
        const val TAG = "ArticleAnnotateCreateModal"
    }
}
