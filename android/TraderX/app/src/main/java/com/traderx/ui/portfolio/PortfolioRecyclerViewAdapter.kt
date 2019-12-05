package com.traderx.ui.portfolio



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.traderx.R
import com.traderx.api.response.PortfolioResponse
import kotlinx.android.synthetic.main.item_user.view.*

class PortfolioRecyclerViewAdapter(
    private val mValues: List<PortfolioResponse>
) : RecyclerView.Adapter<PortfolioRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_portfolio, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mUserNameView.text = item.name

        with(holder.mView) {
            setOnClickListener {

             //   val action =
               //     UserSearchFragmentDirections.actionNavigationUserSearchToNavigationUser(item.name)

//                findNavController().navigate(action)

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
