package com.traderx.ui.profile


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.traderx.R
import com.traderx.api.response.FollowerResponse
import com.traderx.db.User
import kotlinx.android.synthetic.main.item_follower.view.*

class FollowersRecyclerViewAdapter(
    private val followers: List<FollowerResponse>
) : RecyclerView.Adapter<FollowersRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_follower, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.username.text = followers[position].username
    }

    override fun getItemCount(): Int = followers.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.item_username
    }
}
