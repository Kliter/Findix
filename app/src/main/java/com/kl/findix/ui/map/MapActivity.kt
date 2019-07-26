package com.kl.findix.ui.map

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.kl.findix.R
import com.kl.findix.databinding.ActivityMapBinding
import dagger.android.AndroidInjection

class MapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMapBinding>(this, R.layout.activity_map)
    }
}
