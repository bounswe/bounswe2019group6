package com.traderx.ui.profile

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.traderx.R
import com.traderx.api.response.AssetsResponse
import kotlinx.android.synthetic.main.item_asset.view.*
import kotlinx.android.synthetic.main.layout_asset_modal.view.*
import kotlinx.android.synthetic.main.layout_equipment_modal.view.*
import kotlinx.android.synthetic.main.layout_portfolio_modal.view.dialog_cancel_btn
import java.math.BigDecimal
import java.math.RoundingMode

class AssetRecyclerViewAdapter(
    private var assets: ArrayList<AssetsResponse>,
    private var context: Context ,
    private val onSell: (code: String, amount: Double, () -> Unit) -> Unit,
    private val actionNavDirection: (code: String ) -> NavDirections
) : RecyclerView.Adapter<AssetRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_asset, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.asset.text = assets[position].code
        holder.stock.text = BigDecimal(assets[position].amount.toString()).setScale(2, RoundingMode.HALF_EVEN).toPlainString()
        if (assets[position].code == "USD"){
            holder.sellButton.isEnabled = false
            holder.sellButton.background = holder.sellButton.context.resources.getDrawable(R.color.light_gray)
        }

        holder.sellButton.setOnClickListener {
            val pDialogView = LayoutInflater.from(context)
                .inflate(R.layout.layout_asset_modal, null)

            val builder = AlertDialog.Builder(context)
                .setView(pDialogView).setTitle("Sell Equipment")
            val pAlertDialog = builder.show()

            pDialogView.dialog_sell_equipment.setOnClickListener {
                if(pDialogView.dialog_asset_val.text.isEmpty()){
                    return@setOnClickListener
                }
                val amount = pDialogView.dialog_asset_val.text.ifEmpty { "0.0" }
                onSell(assets[position].code, java.lang.Double.parseDouble(amount.toString())){
                    notifyDataSetChanged()
                    pAlertDialog.dismiss()
                }



            }
            pDialogView.dialog_cancel_btn.setOnClickListener {
                pAlertDialog.dismiss()
            }
        }

        with(holder.view) {
            setOnClickListener {
                findNavController().navigate(actionNavDirection(assets[holder.adapterPosition].code))
            }
        }


    }

    override fun getItemCount(): Int = assets.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val asset : TextView = view.asset_equipment_name
        val stock : TextView = view.asset_owned_stock
        val sellButton : Button = view.asset_sell_action


    }
}
