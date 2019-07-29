package com.kl.findix.ui.map

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import dagger.android.AndroidInjection
import com.kl.findix.R
import kotlinx.android.synthetic.main.activity_maps.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import com.kl.findix.ui.login.LoginActivity
import com.kl.findix.viewmodel.LoginViewModel
import com.kl.findix.viewmodel.ViewModelFactory
import javax.inject.Inject

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        private const val TAG = "MapsActivity"
        fun newInstance(context: Context) = Intent(context, MapsActivity::class.java)
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private var mMap: GoogleMap? = null
    private lateinit var mLoginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        mLoginViewModel = ViewModelProviders.of(this, mViewModelFactory).get(LoginViewModel::class.java)
        setupWidgets()
    }

    override fun onStart() {
        super.onStart()
        if (mLoginViewModel.getCurrentSignInUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupWidgets() {
        setupMap()
    }

    private fun setupMap() {
        val mapFragment: SupportMapFragment = map as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap

        mMap?.let {
            val sydney = LatLng(-34.0, 151.0)// Tmp implementation.
            it.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
            it.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        }
    }
}
