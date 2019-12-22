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
import java.util.regex.Pattern

class AnnotationRecyclerViewAdapter(
    private val authUsername: String,
    private val annotations: ArrayList<AnnotationResponse>,
    private val onDelete: (id: Int, doOnSuccess: () -> Unit) -> Unit
) : RecyclerView.Adapter<AnnotationRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_annotation, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val annotationItem = annotations[position]
        with(holder) {
            val username = extractUsername(annotationItem.creator)
            annotation.text = annotationItem.body.value
            author.text = username
            date.text = annotationItem.created

            if (authUsername != username) {
                deleteAction.visibility = View.GONE
//                editAction.visibility = View.GONE
            } else {
                deleteAction.setOnClickListener {
                    onDelete(annotations[adapterPosition].id) {
                        annotations.removeAt(adapterPosition)
                        notifyItemRemoved(adapterPosition)
                    }
                }

//                editAction.setOnClickListener {
//                    onEdit(comments[adapterPosition].id, comments[adapterPosition].comment) { id, message ->
//                        updateItem(id, message)
//                    }
//                }
            }
        }
    }

    override fun getItemCount(): Int = annotations.size

    private fun extractUsername(url: String): String {
        val pattern = Pattern.compile("^(?:[^/]*[/]){4}(.*)/[a-z]*\$")
        val matcher = pattern.matcher(url)
        return if (matcher.find()) matcher.group(1) else ""
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val annotation: TextView = view.annotation
        val author: TextView = view.author
        val deleteAction: ImageView = view.delete_action
        val date: TextView = view.time
//        val editAction: ImageView = view.edit_action
    }
}