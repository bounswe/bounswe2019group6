package com.traderx.ui.article

import android.os.Bundle
import android.text.LoginFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.traderx.R
import com.traderx.api.response.AnnotationResponse

class ArticleAnnotateListModal (
    private val authUsername: String,
    private val annotations: ArrayList<AnnotationResponse>
) : BottomSheetDialogFragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.modal_annotation_list, container, false)

        val viewManager = LinearLayoutManager(context)
        recyclerView = view.findViewById<RecyclerView>(R.id.annotations_list).apply {
            layoutManager = viewManager
            adapter = AnnotationRecyclerViewAdapter(authUsername, annotations)
        }

        return view
    }

    companion object {
        const val TAG = "ArticleAnnotationListModal"
    }
}