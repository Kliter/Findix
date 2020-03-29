package com.kl.findix.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kl.findix.R
import com.kl.findix.databinding.ActivityMainBinding
import com.kl.findix.di.ViewModelFactory
import com.kl.findix.util.extension.viewModelProvider
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        fun newInstance(context: Context) = Intent(context, MainActivity::class.java)
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var _viewModel: MainViewModel
    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        _viewModel = viewModelProvider(mViewModelFactory)
        binding.apply {
            viewModel = _viewModel
            lifecycleOwner = this@MainActivity
        }
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

    }
}