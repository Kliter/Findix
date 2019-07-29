package com.kl.findix.ui.map

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.kl.findix.databinding.ActivityMapBinding
import dagger.android.AndroidInjection
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kl.findix.R


class MapActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MapActivity"

        fun newInstance(context: Context) = Intent(context, MapActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMapBinding>(this, R.layout.activity_map)
    }

    override fun onStart() {
        super.onStart()
    }
}
