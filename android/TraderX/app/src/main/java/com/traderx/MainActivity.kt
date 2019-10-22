package com.traderx

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.traderx.ui.profile.AuthUserProfileActivity
import com.traderx.ui.search.UserSearchActivity

class MainActivity : AppCompatActivity() {

    private lateinit var profileButton: Button
    private lateinit var followButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        profileButton = findViewById(R.id.main_profile)
        followButton = findViewById(R.id.main_follow)

        profileButton.setOnClickListener {
            startActivity(Intent(this, AuthUserProfileActivity::class.java))
        }

        followButton.setOnClickListener {
            startActivity(Intent(this, UserSearchActivity::class.java))
        }
    }
}
