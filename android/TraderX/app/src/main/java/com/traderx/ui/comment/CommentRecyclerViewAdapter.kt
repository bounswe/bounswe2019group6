package com.traderx.ui.comment

import android.annotation.TargetApi
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.traderx.R
import com.traderx.api.response.CommentResponse
import com.traderx.type.VoteType
import kotlinx.android.synthetic.main.item_comment.view.*

@TargetApi(Build.VERSION_CODES.N)
class CommentRecyclerViewAdapter(
    private val comments: ArrayList<CommentResponse>,
    private val username: String?,
    private val onDelete: (id: Int, doOnSuccess: () -> Unit) -> Unit,
    private val onEdit: (id: Int, message: String, doOnSuccess: (id: Int, message: String) -> Unit) -> Unit,
    private val onVote: (comment: CommentResponse, vote: VoteType, doOnVote: () -> Unit) -> Unit
) : RecyclerView.Adapter<CommentRecyclerViewAdapter.ViewHolder>() {

    private var dateFormatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val com = comments[position]

            author.text = com.author
            comment.text = com.comment
            likes.text = com.likes.toString()
            dislikes.text = com.dislikes.toString()
            lastModifiedTime.text = dateFormatter.format(com.lastModifiedTime)

            if (username != com.author) {
                deleteAction.visibility = View.GONE
                editAction.visibility = View.GONE

                likeAction.setOnClickListener {
                    onVote(com, VoteType.LIKED) {
                        notifyItemChanged(adapterPosition)
                    }
                }

                dislikeAction.setOnClickListener {
                    onVote(com, VoteType.DISLIKED) {
                        notifyItemChanged(adapterPosition)
                    }
                }

            } else {
                voteLayout.visibility = View.GONE

                deleteAction.setOnClickListener {
                    onDelete(comments[adapterPosition].id) {
                        comments.removeAt(adapterPosition)
                        notifyItemRemoved(adapterPosition)
                    }
                }

                editAction.setOnClickListener {
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
            comments[index].comment = message

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
        val likeAction: ImageView = view.like_action
        val dislikeAction: ImageView = view.dislike_action
        val likes: TextView = view.likes
        val dislikes: TextView = view.dislikes
        val voteLayout: LinearLayout = view.vote_layout
    }
}