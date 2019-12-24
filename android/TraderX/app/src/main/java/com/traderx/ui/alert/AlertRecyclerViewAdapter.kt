package com.traderx.ui.alert

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.traderx.R
import com.traderx.api.response.AlertResponse
import kotlinx.android.synthetic.main.item_alert.view.*

class AlertRecyclerViewAdapter(
    private val alerts: ArrayList<AlertResponse>,
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val onDeleteAction: (id: Int, doOnSuccess: () -> Unit) -> Unit
) : RecyclerView.Adapter<AlertRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alert, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.code.text = alerts[position].code
        holder.alertPhrase.text = context.getString(
            R.string.alert_phrase,
            alerts[position].orderType.request,
            alerts[position].alertType.request,
            alerts[position].limitValue.toString(),
            alerts[position].amount.toString()
        )

        holder.view.setOnLongClickListener {
            val alertDeleteModal = AlertDeleteModal { modal ->
                onDeleteAction(
                    alerts[holder.adapterPosition].id
                ) {
                    alerts.removeAt(holder.adapterPosition)
                    notifyItemRemoved(holder.adapterPosition)
                    modal.dismiss()
                }
            }

            alertDeleteModal.show(fragmentManager, AlertDeleteModal.TAG)

            true
        }
    }

    override fun getItemCount(): Int = alerts.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val code: TextView = view.code
        val alertPhrase = view.alert_phrase
    }
}