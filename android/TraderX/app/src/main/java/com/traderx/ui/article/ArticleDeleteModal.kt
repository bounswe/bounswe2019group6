package com.traderx.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.traderx.R

class ArticleDeleteModal(
    val onDeleteAction: (modal: BottomSheetDialogFragment) -> Unit
) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.modal_delete, container, false)

        view.findViewById<LinearLayout>(R.id.delete)?.let { layout ->
            layout.setOnClickListener {
                onDeleteAction(this)
            }
        }

        return view
    }

    companion object {
        const val TAG = "ArticleDeleteModal"
    }
}
