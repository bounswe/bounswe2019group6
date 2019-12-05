package com.traderx.ui.equipment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.util.toHalf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.traderx.R
import com.traderx.api.response.EquipmentsResponse
import com.traderx.enum.EquipmentType
import kotlinx.android.synthetic.main.item_equipment.view.*

class EquipmentRecyclerViewAdapter(
    private val equipments: List<EquipmentsResponse.Equipment>,
    private val base: String,
    private val type: EquipmentType,
    private val context: Context
) : RecyclerView.Adapter<EquipmentRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_equipment, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.code.text = equipments[position].code
        holder.base.text = base

        holder.value.text = when (type) {
            EquipmentType.STOCK -> context.getString(
                R.string.usd_value,
                equipments[position].data.currentValue.toHalf().toString()
            )
            EquipmentType.CRYPTO_CURRENCY -> context.getString(
                R.string.usd_value,
                equipments[position].data.currentValue.toHalf().toString()
            )
            else -> equipments[position].data.currentValue.toHalf().toString()
        }

        holder.stock.text = equipments[position].data.currentStock.toString()

        with(holder.view) {
            setOnClickListener {
                findNavController().navigate(
                    EquipmentsFragmentDirections.actionNavigationEquipmentsToNavigationEquipment(
                        equipments[position].code
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int = equipments.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val code: TextView = view.code
        val base: TextView = view.base
        val value: TextView = view.value
        val stock: TextView = view.stock
    }
}