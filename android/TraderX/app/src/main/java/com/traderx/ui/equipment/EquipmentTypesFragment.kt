package com.traderx.ui.equipment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController

import com.traderx.R
import com.traderx.type.EquipmentType

/**
 * A simple [Fragment] subclass.
 */
class EquipmentTypesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_equipment_types, container, false)

        root.findViewById<LinearLayout>(R.id.equipment_types_crypto_currencies_action)?.let {
            it.setOnClickListener {
                findNavController().navigate(EquipmentTypesFragmentDirections.actionNavigationEquipmentTypesToNavigationEquipments(EquipmentType.CRYPTO_CURRENCY))
            }
        }

        root.findViewById<LinearLayout>(R.id.equipment_types_currencies_action)?.let {
            it.setOnClickListener {
                findNavController().navigate(EquipmentTypesFragmentDirections.actionNavigationEquipmentTypesToNavigationEquipments(EquipmentType.CURRENCY))
            }
        }

        root.findViewById<LinearLayout>(R.id.equipment_types_stocks_action)?.let {
            it.setOnClickListener {
                findNavController().navigate(EquipmentTypesFragmentDirections.actionNavigationEquipmentTypesToNavigationEquipments(EquipmentType.STOCK))
            }
        }

        return root
    }

}
