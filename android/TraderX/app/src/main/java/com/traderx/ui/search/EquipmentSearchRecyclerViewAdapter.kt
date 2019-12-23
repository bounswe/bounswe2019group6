package com.traderx.ui.search


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.traderx.R
import com.traderx.api.response.EquipmentSearchResponse
import kotlinx.android.synthetic.main.item_user.view.*

class EquipmentSearchRecyclerViewAdapter(
    private val mValues: List<EquipmentSearchResponse>
) : RecyclerView.Adapter<EquipmentSearchRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mUserNameView.text = item.equipmentName

        with(holder.mView) {
            setOnClickListener {
                findNavController().navigate(
                    SearchFragmentDirections.actionNavigationUserSearchToNavigationEquipment(
                        item.code
                    )
                )
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
