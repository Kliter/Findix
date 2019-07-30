package com.kl.findix.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.kl.findix.R
import com.kl.findix.databinding.ActivityProfileBinding
import com.kl.findix.util.setupBottomNavigationView
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.bottom_navigation_view

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityProfileBinding>(this, R.layout.activity_profile)
    }

    override fun onStart() {
        super.onStart()
        bottom_navigation_view.selectedItemId = R.id.action_profile
        setupBottomNavigationView(this, bottom_navigation_view)
    }
}
