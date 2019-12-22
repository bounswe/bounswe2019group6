package com.traderx.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.github.razir.progressbutton.isProgressActive
import com.github.razir.progressbutton.showProgress
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.traderx.R

class ArticleAnnotateCreateModal(
    val onCreate: (modal: BottomSheetDialogFragment, annotation: String) -> Unit
    ) : BottomSheetDialogFragment() {

    private lateinit var annotation: EditText

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
                        onCreate(this, annotation.text.toString())
                    }
                } else {
                    //ERROR
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
