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
import com.traderx.api.ErrorHandler
import com.traderx.api.response.FollowerResponse
import com.traderx.util.Helper
import com.traderx.viewmodel.AuthUserViewModel
import kotlinx.android.synthetic.main.item_pending_request.view.*

class PendingRequestRecyclerViewAdapter(
    private val pendingRequests: ArrayList<FollowerResponse>,
    private val acceptRequest: (username: String, () -> Unit) -> Unit,
    private val declineRequest: (username: String, () -> Unit) -> Unit,
    private val actionNavDirection: (username: String ) -> NavDirections
) : RecyclerView.Adapter<PendingRequestRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pending_request, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.username.text = pendingRequests[position].username

        with(holder.view) {
            setOnClickListener {
                findNavController().navigate(actionNavDirection(pendingRequests[position].username))
            }
        }

        holder.acceptAction.setOnClickListener {
            acceptRequest(pendingRequests[position].username) {
                pendingRequests.removeAt(position)
                notifyItemRemoved(position)
            }
        }

        holder.declineAction.setOnClickListener {
            declineRequest(pendingRequests[position].username) {
                pendingRequests.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }

    override fun getItemCount(): Int = pendingRequests.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.username
        val acceptAction: ImageView = view.accept_action
        val declineAction: ImageView = view.decline_action
    }
}
