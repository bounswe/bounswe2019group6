package com.traderx.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.traderx.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        root.findViewById<LinearLayout>(R.id.home_article_action)?.let {
            it.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToNavigationArticles())
            }
        }

        root.findViewById<LinearLayout>(R.id.home_equipment_action)?.let {
            it.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToNavigationTraderEquipment())
            }
        }

        return root
    }
}
