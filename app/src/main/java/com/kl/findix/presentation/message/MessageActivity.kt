package com.kl.findix.presentation.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.kl.findix.R
import com.kl.findix.databinding.ActivityMessageBinding
import com.kl.findix.util.setupBottomNavigationView
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_message.bottom_navigation_view

class MessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMessageBinding>(this, R.layout.activity_message)
    }

    override fun onResume() {
        super.onResume()
        bottom_navigation_view.selectedItemId = R.id.message
        setupBottomNavigationView(this, bottom_navigation_view)
    }
}
