package com.traderx.ui.traderequipment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.traderx.R

class TraderEquipmentFragment : Fragment() {

    companion object {
        fun newInstance() = TraderEquipmentFragment()
    }

    private lateinit var viewModel: TraderEquipmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trader_equipment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TraderEquipmentViewModel::class.java)
        // TODO: Use the ViewModel
    }

}