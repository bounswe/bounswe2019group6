package com.traderx.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.traderx.R
import kotlinx.android.synthetic.main.item_user_search.view.*

class UserSearchSkeletonRecyclerViewAdapter(
    private val numSkeleton: Int
): RecyclerView.Adapter<UserSearchSkeletonRecyclerViewAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_skeleton, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = numSkeleton

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView)
}