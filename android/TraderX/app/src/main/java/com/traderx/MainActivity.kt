package com.traderx

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.traderx.util.FragmentTitleListeners

class MainActivity : AppCompatActivity(), FragmentTitleListeners {

    private lateinit var fragmentTitleLayout : RelativeLayout
    private lateinit var fragmentTitleText: TextView
    private lateinit var fragmentTitleButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        fragmentTitleLayout = findViewById(R.id.activity_bar_layout)
        fragmentTitleText = findViewById(R.id.activity_bar_text)
        fragmentTitleButton = findViewById(R.id.activity_bar_button)

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
        fragmentTitleText.visibility = View.VISIBLE
        fragmentTitleLayout.visibility = View.VISIBLE
    }

    override fun showFragmentActionButton(text: String, onClick: (button: Button) -> Unit) {
        fragmentTitleButton.text = text
        fragmentTitleButton.visibility = View.VISIBLE
        fragmentTitleLayout.visibility = View.VISIBLE

        fragmentTitleButton.setOnClickListener {
            onClick(it as Button)
        }
    }

    override fun hideFragmentActionButton() {
        fragmentTitleButton.visibility = View.INVISIBLE
    }

    private fun hideFragmentTitle() {
        fragmentTitleLayout.visibility = View.GONE
        fragmentTitleButton.visibility = View.INVISIBLE
        fragmentTitleText.visibility = View.GONE
    }
}
