package com.traderx.ui.comment

import android.annotation.TargetApi
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.traderx.R
import com.traderx.api.response.CommentResponse
import kotlinx.android.synthetic.main.item_comment.view.*

@TargetApi(Build.VERSION_CODES.N)
class CommentRecyclerViewAdapter(
    private val comments: ArrayList<CommentResponse>,
    private val username: String?,
    private val onDelete: (id: Int, doOnSuccess: () -> Unit) -> Unit,
    private val onEdit: (id: Int, message: String, doOnSuccess: (id: Int, message: String) -> Unit) -> Unit
) : RecyclerView.Adapter<CommentRecyclerViewAdapter.ViewHolder>() {

    private var dateFormatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            author.text = comments[position].author
            comment.text = comments[position].comment
            lastModifiedTime.text = dateFormatter.format(comments[position].lastModifiedTime)

            if (username != comments[position].author) {
                deleteAction.visibility = View.GONE
                editAction.visibility = View.GONE
            } else {
                holder.deleteAction.setOnClickListener {
                    onDelete(comments[adapterPosition].id) {
                        comments.removeAt(adapterPosition)
                        notifyItemRemoved(adapterPosition)
                    }
                }

                holder.editAction.setOnClickListener {
                    onEdit(comments[adapterPosition].id, comments[adapterPosition].comment) { id, message ->
                        updateItem(id, message)
                    }
                }
            }
        }
    }

    private fun updateItem(id: Int, message: String) {
        val index = comments.indexOfFirst { item -> item.id == id }
        if (index != -1) {
            val comment = comments[index]
                comments[index] = CommentResponse(
                    comment.author,
                    message,
                    comment.equipment,
                    comment.id,
                    comment.lastModifiedTime
                )

            notifyItemChanged(index)
        }
    }

    override fun getItemCount(): Int = comments.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val author: TextView = view.author
        val comment: TextView = view.comment
        val lastModifiedTime: TextView = view.time
        val deleteAction: ImageView = view.delete_action
        val editAction: ImageView = view.edit_action
    }
}