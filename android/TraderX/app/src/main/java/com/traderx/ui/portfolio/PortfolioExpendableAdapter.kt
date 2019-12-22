package com.traderx.ui.portfolio

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.traderx.R
import com.traderx.api.response.PortfolioEquipment
import java.util.stream.Collectors

class PortfolioExpendableAdapter internal constructor(
    private val context: Context,
    private val titleList: List<String>,
    private val dataList: HashMap<String, List<PortfolioEquipment>>) : BaseExpandableListAdapter() {

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return this.dataList[this.titleList[listPosition]]!![expandedListPosition]
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return listPosition.times(10000).plus(expandedListPosition).toLong()
    }

    override fun getChildView(listPosition: Int, expandedListPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        var childView = convertView

        if (childView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            childView = layoutInflater.inflate(R.layout.exp_portfolio_child, null)
        }
        val expandedListTextView = childView!!.findViewById<TextView>(R.id.portfolio_equipment_child)
        val childCheckBox = childView.findViewById<CheckBox>(R.id.equipment_checkbox)
        val equipment = getChild(listPosition, expandedListPosition) as PortfolioEquipment

        childCheckBox.setOnClickListener {
            val key = titleList[listPosition]
            dataList[key]?.get(expandedListPosition)?.enabled = (!dataList[key]?.get(expandedListPosition)?.enabled!!)
            childCheckBox.isChecked = dataList[key]?.get(expandedListPosition)?.enabled!!
        }

        expandedListTextView.text = equipment.code
        childCheckBox.isChecked = equipment.enabled
        return childView
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return this.dataList[this.titleList[listPosition]]!!.size
    }

    override fun getGroup(listPosition: Int): Any {
        return this.titleList[listPosition]
    }

    override fun getGroupCount(): Int {
        return this.titleList.size
    }

    fun getSelecteds(): List<String> {
        val equipments = arrayListOf<String>()

        for (title in  this.titleList){
            for (eq in dataList[title]!!){
                if (eq.enabled ){
                    equipments.add(eq.code)
                }
            }
        }

        return equipments
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(listPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        var groupView = convertView
        val listTitle = getGroup(listPosition) as String
        if (convertView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            groupView = layoutInflater.inflate(R.layout.exp_portfolio_group, null)
        }
        val listTitleTextView = groupView!!.findViewById<TextView>(R.id.portfolio_equipment_group)
        listTitleTextView.text = listTitle
        return groupView
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }
}
