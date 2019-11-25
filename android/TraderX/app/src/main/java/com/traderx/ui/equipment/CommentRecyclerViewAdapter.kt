package com.traderx.ui.equipment

import android.annotation.TargetApi
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.traderx.R
import com.traderx.api.response.CommentResponse
import kotlinx.android.synthetic.main.item_comment.view.*

@TargetApi(Build.VERSION_CODES.N)
class CommentRecyclerViewAdapter(
    private val comments: List<CommentResponse>
) : RecyclerView.Adapter<CommentRecyclerViewAdapter.ViewHolder>() {

    private var dateFormatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.author.text = comments[position].author
        holder.comment.text = comments[position].comment
        holder.lastModifiedTime.text = dateFormatter.format(comments[position].lastModifiedTime)
    }

    override fun getItemCount(): Int = comments.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val author: TextView = view.author
        val comment: TextView = view.comment
        val lastModifiedTime: TextView = view.time
    }
}