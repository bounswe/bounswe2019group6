package com.traderx.ui.event

import android.annotation.TargetApi
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.traderx.R
import com.traderx.api.response.EventResponse
import kotlinx.android.synthetic.main.item_event.view.*

@TargetApi(Build.VERSION_CODES.N)
class EventRecyclerViewAdapter(
    private val events: List<EventResponse>
) : RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eventItem = events[position]
        with(holder) {
            event.text = eventItem.event
            country.text = eventItem.country
            importance.rating = eventItem.importance.toFloat()
            date.text = eventItem.date
        }
    }

    override fun getItemCount(): Int = events.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val event: TextView = view.event
        val country: TextView = view.country
        val importance: RatingBar = view.importance
        val date: TextView = view.date
    }
}
