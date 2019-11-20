package com.traderx.ui.equipment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.traderx.R
import com.traderx.db.Equipment
import kotlinx.android.synthetic.main.item_equipment.view.*

class EquipmentRecyclerViewAdapter(
    private val equipments: List<String>
) : RecyclerView.Adapter<EquipmentRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_equipment, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = equipments[position]

        with(holder.view) {
            setOnClickListener {
                findNavController().navigate(EquipmentsFragmentDirections.actionNavigationEquipmentsToNavigationEquipment(equipments[position]))
            }
        }
    }

    override fun getItemCount(): Int = equipments.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.item_equipment_title
    }
}