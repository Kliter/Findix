package com.kl.findix.util

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kl.findix.R
import com.kl.findix.presentation.order.OrderActivity
import com.kl.findix.presentation.map.MapsActivity
import com.kl.findix.presentation.profile.ProfileActivity

fun setupBottomNavigationView(activity: AppCompatActivity, bottomNavigationView: BottomNavigationView) {
    bottomNavigationView.setOnNavigationItemSelectedListener {
        val intent = Intent()
        when (it.itemId) {
            R.id.action_map -> {
                intent.setClass(activity, MapsActivity::class.java)
            }
            R.id.action_profile -> {
                intent.setClass(activity, ProfileActivity::class.java)
            }
            R.id.action_order -> {
                intent.setClass(activity, OrderActivity::class.java)
            }
        }
        activity.startActivity(intent)
        return@setOnNavigationItemSelectedListener true
    }
}