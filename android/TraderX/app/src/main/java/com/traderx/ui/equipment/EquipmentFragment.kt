package com.traderx.ui.equipment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.traderx.R
import com.traderx.util.FragmentTitleEmitters
import com.traderx.util.FragmentTitleListeners

class EquipmentFragment : Fragment(), FragmentTitleEmitters {
    private var equipmentName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            equipmentName = it.getString(ARG_EQUIPMENT_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setFragmentTitle(context, equipmentName)

        val root = inflater.inflate(R.layout.fragment_equipment, container, false)
        return root
    }

    override fun setFragmentTitle(context: Context?, title: String?) {
        if(context is FragmentTitleListeners) {
            context.showFragmentTitle(title)
        }
    }

    companion object {
        private const val ARG_EQUIPMENT_NAME = "equipment_name"
    }
}
