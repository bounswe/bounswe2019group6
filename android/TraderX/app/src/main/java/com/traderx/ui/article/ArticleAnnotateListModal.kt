package com.traderx.ui.article

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.traderx.R
import com.traderx.api.ErrorHandler
import com.traderx.api.response.AnnotationResponse
import com.traderx.util.Helper
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

class ArticleAnnotateListModal(
    private val authUsername: String,
    private val annotations: ArrayList<AnnotationResponse>,
    private val onDelete: (id: Int) -> Completable
) : BottomSheetDialogFragment() {

    private lateinit var recyclerView: RecyclerView

    private val disposable = CompositeDisposable()

    private var isOnRequest = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.modal_annotation_list, container, false)

        val viewManager = LinearLayoutManager(context)
        recyclerView = view.findViewById<RecyclerView>(R.id.annotations_list).apply {
            layoutManager = viewManager
            adapter = AnnotationRecyclerViewAdapter(authUsername, annotations) { id, doOnSuccess ->
                if (!isOnRequest) {
                    disposable.add(
                        onDelete(id)
                            .compose(Helper.applyCompletableSchedulers())
                            .doOnComplete {
                                doOnSuccess()
                                isOnRequest = false
                            }.subscribe({
                                Snackbar.make(
                                    requireView(),
                                    getString(R.string.annotation_delete_success),
                                    Snackbar.LENGTH_SHORT
                                ).show()

                            }, { ErrorHandler.handleError(it, context as Context) })
                    )
                }
            }
        }

        return view
    }

    companion object {
        const val TAG = "ArticleAnnotationListModal"
    }
}