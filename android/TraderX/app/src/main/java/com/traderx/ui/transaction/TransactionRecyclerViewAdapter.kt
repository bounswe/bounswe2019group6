package com.traderx.ui.transaction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.traderx.R
import com.traderx.api.response.TransactionsResponse
import kotlinx.android.synthetic.main.item_transaction.view.*

class TransactionRecyclerViewAdapter(
    private val transactions: List<TransactionsResponse>
) : RecyclerView.Adapter<TransactionRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.amount.text = transactions[position].amount.toString()
        holder.code.text = transactions[position].equipment
        holder.date.text = transactions[position].createdAt
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