package com.kl.findix.presentation.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.kl.findix.R
import com.kl.findix.databinding.ActivityListBinding
import com.kl.findix.util.setupBottomNavigationView
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_list.bottom_navigation_view

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityListBinding>(this, R.layout.activity_list)
    }

    override fun onResume() {
        super.onResume()
        bottom_navigation_view.selectedItemId = R.id.action_list
        setupBottomNavigationView(this, bottom_navigation_view)
    }
}
