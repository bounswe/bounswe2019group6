package com.traderx.ui.portfolio



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.traderx.R
import com.traderx.api.response.PortfolioResponse
import kotlinx.android.synthetic.main.item_portfolio.view.*

class PortfolioRecyclerViewAdapter(
    private val portfolios: ArrayList<PortfolioResponse>,
    private val deleteAction: (name: String, () -> Unit) -> Unit,
    private val actionNavDirection: (name: String ) -> NavDirections
    ) : RecyclerView.Adapter<PortfolioRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_portfolio, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = portfolios[position]
        holder.mPortfolioName.text = item.portfolioName

        with(holder.mView) {
            setOnClickListener {
                findNavController().navigate(actionNavDirection(portfolios[holder.adapterPosition].portfolioName))
            }
        }

        holder.deleteAction.setOnClickListener {
            deleteAction(portfolios[holder.adapterPosition].portfolioName) {
                portfolios.removeAt(holder.adapterPosition)
                notifyItemRemoved(holder.adapterPosition)
            }
        }

    }

    fun addData(portfolio:PortfolioResponse) {
        portfolios.add(portfolio)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = portfolios.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mPortfolioName: TextView = mView.portfolio_name
        val deleteAction: ImageView = mView.delete_portfolio_action
    }
}
