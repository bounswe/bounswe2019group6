package com.traderx.ui.article

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.traderx.R
import com.traderx.api.response.AnnotationResponse
import kotlinx.android.synthetic.main.item_annotation.view.*

class AnnotationRecyclerViewAdapter(
    private val authUsername: String,
    private val annotations: ArrayList<AnnotationResponse>
) : RecyclerView.Adapter<AnnotationRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_annotation, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val annotationItem = annotations[position]
        with(holder) {
            annotation.text = annotationItem.body.value

            if (authUsername != annotationItem.creator) {
                deleteAction.visibility = View.GONE
                editAction.visibility = View.GONE

            } else {
//                deleteAction.setOnClickListener {
//                    onDelete(comments[adapterPosition].id) {
//                        comments.removeAt(adapterPosition)
//                        notifyItemRemoved(adapterPosition)
//                    }
//                }
//
//                editAction.setOnClickListener {
//                    onEdit(comments[adapterPosition].id, comments[adapterPosition].comment) { id, message ->
//                        updateItem(id, message)
//                    }
//                }
            }
        }
    }

    override fun getItemCount(): Int = annotations.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val annotation: TextView = view.annotation
        val deleteAction: ImageView = view.delete_action
        val editAction: ImageView = view.edit_action
    }
}