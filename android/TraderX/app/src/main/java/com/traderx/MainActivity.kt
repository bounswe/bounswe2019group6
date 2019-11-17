package com.traderx

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.traderx.util.FragmentTitleListeners

class MainActivity : AppCompatActivity(), FragmentTitleListeners {

    private lateinit var fragmentTitleLayout : LinearLayout
    private lateinit var fragmentTitleText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        fragmentTitleLayout = findViewById(R.id.activity_bar_layout)
        fragmentTitleText = findViewById(R.id.activity_bar_text)
        
//        Drop the support for Action Bar
//        val appBarConfiguration =
//            AppBarConfiguration(setOf(R.id.navigation_home, R.id.navigation_trader_equipment))
//
//        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, _, _ ->
            hideFragmentTitle()
        }

        navView.setupWithNavController(navController)
    }

    override fun showFragmentTitle(title: String?) {
        fragmentTitleText.text = title
        fragmentTitleLayout.visibility = View.VISIBLE
    }

    private fun hideFragmentTitle() {
        fragmentTitleLayout.visibility = View.GONE
    }
}
