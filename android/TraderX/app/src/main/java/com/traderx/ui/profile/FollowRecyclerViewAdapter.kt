package com.traderx.ui.profile


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.traderx.R
import com.traderx.api.response.FollowerResponse
import kotlinx.android.synthetic.main.item_follow.view.*

class FollowRecyclerViewAdapter(
    private var followers: ArrayList<FollowerResponse>,
    private val unfollowAction: (username: String, () -> Unit) -> Unit,
    private val actionNavDirection: (username: String ) -> NavDirections,
    private val authUser: Boolean
) : RecyclerView.Adapter<FollowRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_follow, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.username.text = followers[position].username

        with(holder.view) {
            setOnClickListener {
                findNavController().navigate(actionNavDirection(followers[holder.adapterPosition].username))
            }
        }

        if(!authUser) {
            holder.unfollowAction.visibility = View.GONE
        } else {
            holder.unfollowAction.setOnClickListener {
                unfollowAction(followers[holder.adapterPosition].username) {
                    followers.removeAt(holder.adapterPosition)
                    notifyItemRemoved(holder.adapterPosition)
                }
            }
        }
    }

    override fun getItemCount(): Int = followers.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.username
        val unfollowAction: ImageView = view.unfollow_action

    }
}
