package com.traderx.ui.search


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.traderx.R
import com.traderx.api.response.UserAll
import com.traderx.ui.profile.UserFragment
import kotlinx.android.synthetic.main.item_user_search.view.*

class UserSearchRecyclerViewAdapter(
    private val mValues: List<UserAll>
//    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<UserSearchRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mUserNameView.text = item.username

        with(holder.mView) {
            setOnClickListener {

                val action =
                    UserSearchFragmentDirections.actionNavigationUserSearchToNavigationUser(item.username)

                findNavController().navigate(action)

            }
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mUserNameView: TextView = mView.item_username

        override fun toString(): String {
            return super.toString() + " '" + mUserNameView.text + "'"
        }
    }
}
