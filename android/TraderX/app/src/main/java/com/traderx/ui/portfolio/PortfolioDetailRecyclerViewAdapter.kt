package com.traderx.ui.portfolio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.traderx.R
import com.traderx.api.response.EquipmentResponse
import kotlinx.android.synthetic.main.item_portfolio_equipment.view.*

class PortfolioDetailRecyclerViewAdapter(
    private val equipments: ArrayList<EquipmentResponse.Equipment>,
    private val deleteAction: ( code: String ,  () -> Unit) -> Unit
) : RecyclerView.Adapter<PortfolioDetailRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_portfolio_equipment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = equipments[position]
        holder.equipmentName.text = item.code
        holder.equipmentVal.text = item.currentValue.toString()

        with(holder.view) {
            setOnClickListener {
                findNavController().navigate(
                    PortfolioFragmentDirections.actionNavigationEquipmentsToNavigationEquipment(
                        equipments[position].code
                    )
                )

            }
        }

        holder.deleteAction.setOnClickListener {
            deleteAction(equipments[holder.adapterPosition].code) {
                equipments.removeAt(holder.adapterPosition)
                notifyItemRemoved(holder.adapterPosition)
            }
        }

    }

    fun setNewData(newEquipments: List<EquipmentResponse.Equipment>) {
        equipments.clear()
        equipments.addAll(newEquipments)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = equipments.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val equipmentName: TextView = view.portfolio_equipment_name
        val equipmentVal: TextView = view.portfolio_equipment_value
        val deleteAction: ImageView = view.delete_portfolio_equipment    }
}
