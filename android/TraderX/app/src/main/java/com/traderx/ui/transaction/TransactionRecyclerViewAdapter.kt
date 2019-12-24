package com.traderx.ui.transaction

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.traderx.R
import com.traderx.api.response.TransactionsResponse
import kotlinx.android.synthetic.main.item_transaction.view.*
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class TransactionRecyclerViewAdapter(
    private val transactions: List<TransactionsResponse>
) : RecyclerView.Adapter<TransactionRecyclerViewAdapter.ViewHolder>() {
    private val formatter = java.text.SimpleDateFormat("E, dd MMM yyyy HH:mm:ss", Locale.US)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)

        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.amount.text = transactions[position].amount.toString()
        holder.code.text = transactions[position].equipment
        holder.date.text = formatter.format(Date.from( Instant.from(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0")
                .withZone(ZoneId.of("UTC"))
                .parse(transactions[position].createdAt)
        )))
        holder.type.text = transactions[position].transactionType
    }

    override fun getItemCount(): Int = transactions.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val amount: TextView = view.amount
        val code: TextView = view.code
        val date: TextView = view.date
        val type: TextView = view.type
    }
}