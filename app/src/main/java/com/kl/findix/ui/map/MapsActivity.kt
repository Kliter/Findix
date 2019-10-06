package com.kl.findix.ui.map

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.kl.findix.R
import com.kl.findix.databinding.ActivityMapsBinding
import com.kl.findix.di.ViewModelFactory
import com.kl.findix.ui.list.ListActivity
import com.kl.findix.ui.login.LoginActivity
import com.kl.findix.ui.message.MessageActivity
import com.kl.findix.ui.profile.ProfileActivity
import com.kl.findix.util.setupBottomNavigationView
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.layout_toolbar.*
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

//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            getLastKnownPermission()
//        } else {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                REQUEST_CODE_PERMISSION
//            )
//        }

    }

//    private fun getLastKnownPermission() {
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            mFusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val location = task.result
//                    location?.let {
//                        val geoPoint = GeoPoint(location.latitude, location.longitude)
//                        Log.d(TAG, "onComplete: latitude: ${location.latitude}")
//                        Log.d(TAG, "onComplete: longitude: ${location.longitude}")
//                    }
//                }
//            }
//        }
//    }

    override fun onStart() {
        super.onStart()
//        _viewModel.signOut()
        if (_viewModel.getCurrentSignInUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNavigationView.selectedItemId = R.id.action_map
        setupBottomNavigationView(this, bottom_navigation_view)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector
}
