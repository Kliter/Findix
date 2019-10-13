package com.kl.findix.presentation.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import com.kl.findix.R
import com.kl.findix.databinding.FragmentMapsBinding
import com.kl.findix.di.ViewModelFactory
import com.kl.findix.navigation.MapsNavigator
import com.kl.findix.util.REQUEST_CODE_PERMISSION
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_maps.*
import kotlinx.android.synthetic.main.fragment_maps.container
import javax.inject.Inject

class MapsFragment : Fragment(), OnMapReadyCallback,
    NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    companion object {
        fun newInstance() = MapsFragment()
        private const val TAG = "MapsFragment"
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory
    @Inject
    lateinit var navigator: MapsNavigator

    private var mMap: GoogleMap? = null

    private lateinit var _viewModel: MapsViewModel
    private val binding: FragmentMapsBinding by lazy {
        DataBindingUtil.inflate<FragmentMapsBinding>(
            layoutInflater,
            R.layout.fragment_maps,
            container,
            false
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewModel = ViewModelProviders.of(this, mViewModelFactory).get(MapsViewModel::class.java)
        binding.apply {
            lifecycleOwner = this@MapsFragment
            viewModel = _viewModel
        }
        setupMap()

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeState()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        mMap?.let { map ->
            val sydney = LatLng(-34.0, 151.0)// Tmp implementation.
            map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
            map.moveCamera(CameraUpdateFactory.newLatLng(sydney))

            context?.let {
                if (ActivityCompat.checkSelfPermission(it,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    map.isMyLocationEnabled = true
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                activity?.finish()
            }
        }
    }

    override fun onQueryTextSubmit(text: String?): Boolean {
        Log.d(TAG, "text is submitted")
        return true
    }

    override fun onQueryTextChange(text: String?): Boolean {
        return false
    }

    fun observeState() {
        //Todo
    }

    private fun setupMap() {
        val mapFragment: SupportMapFragment? = map as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        context?.let {
            when (item.itemId) {
                R.id.action_profile -> {
                    Log.d(TAG, "Profile")
                }
                R.id.action_message -> {
                    Log.d(TAG, "Message")
                }
                R.id.action_list -> {
                    Log.d(TAG, "List")
                }
                else -> {
                    Log.e(TAG, "Invalid item.")
                }
            }
        }

        return true
    }
}