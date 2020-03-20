package com.kl.findix.presentation.map

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kl.findix.R
import com.kl.findix.databinding.ActivityMapsBinding
import com.kl.findix.di.ViewModelFactory
import com.kl.findix.util.setupBottomNavigationView
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_maps.*
import javax.inject.Inject

class MapsActivity : AppCompatActivity(), HasSupportFragmentInjector {

    companion object {
        private const val TAG = "MapsActivityModule"
        fun newInstance(context: Context) = Intent(context, MapsActivity::class.java)
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var _viewModel: MapsViewModel
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private val binding: ActivityMapsBinding by lazy {
        DataBindingUtil.setContentView<ActivityMapsBinding>(this, R.layout.activity_maps)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        _viewModel = ViewModelProviders.of(this, mViewModelFactory).get(MapsViewModel::class.java)
        binding.apply {
            viewModel = _viewModel
            lifecycleOwner = this@MapsActivity
        }

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

    }

    override fun onResume() {
        super.onResume()
        binding.bottomNavigationView.selectedItemId = R.id.action_map
        setupBottomNavigationView(this, bottom_navigation_view)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector
}
