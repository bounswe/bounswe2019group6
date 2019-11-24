package com.traderx.ui.alert

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.traderx.R
import com.traderx.api.response.AlertResponse
import kotlinx.android.synthetic.main.item_alert.view.*

class AlertRecyclerViewAdapter(
    private val alerts: ArrayList<AlertResponse>
) : RecyclerView.Adapter<AlertRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alert, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.code.text = alerts[position].code
    }

    override fun getItemCount(): Int = alerts.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val code: TextView = view.code
    }
}