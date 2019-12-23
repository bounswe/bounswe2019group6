package com.traderx.ui.article

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.traderx.R
import com.traderx.db.Article
import kotlinx.android.synthetic.main.item_article.view.*
import kotlinx.android.synthetic.main.modal_delete.view.*

class ArticleRecyclerViewAdapter(
    private val articles: ArrayList<Article>,
    private val navigate: (id: Int) -> NavDirections,
    private val fragmentManager: FragmentManager,
    private val onDeleteAction: ((id: Int, doOnSuccess: () -> Unit) -> Unit)?
) : RecyclerView.Adapter<ArticleRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.header.text = articles[position].header
        holder.body.text = articles[position].body
        holder.username.text = articles[position].username
        holder.createdAt.text = articles[position].createdAt

        with(holder.view) {
            setOnClickListener {
                findNavController().navigate(
                    navigate(articles[holder.adapterPosition].id)
                )
            }

            onDeleteAction?.let { deleteAction ->

                setOnLongClickListener {
                    val articleDeleteModal = ArticleDeleteModal { modal ->
                        deleteAction(
                            articles[holder.adapterPosition].id
                        ) {
                            articles.removeAt(holder.adapterPosition)
                            notifyItemRemoved(holder.adapterPosition)
                            modal.dismiss()
                        }
                    }

                    articleDeleteModal.show(fragmentManager, ArticleDeleteModal.TAG)

                    true
                }
            }
        }
    }

    override fun getItemCount(): Int = articles.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val header: TextView = view.header
        val body: TextView = view.body
        val username: TextView = view.username
        val createdAt: TextView = view.created_at
    }
}