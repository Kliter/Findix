package com.kl.findix.presentation.map

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.kl.findix.R
import com.kl.findix.databinding.FragmentMapsBinding
import com.kl.findix.di.ViewModelFactory
import com.kl.findix.model.ClusterItem
import com.kl.findix.navigation.MapsNavigator
import com.kl.findix.presentation.login.LoginActivity
import com.kl.findix.util.REQUEST_CODE_PERMISSION
import com.kl.findix.util.nonNullObserve
import com.kl.findix.util.safeLet
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class MapsFragment : Fragment(), OnMapReadyCallback {

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
    private lateinit var binding: FragmentMapsBinding
    private lateinit var mLocationProviderClient: FusedLocationProviderClient
    private lateinit var mClusterManager: ClusterManager<ClusterItem>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(activity as Activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewModel = ViewModelProviders.of(this, mViewModelFactory).get(MapsViewModel::class.java)
        binding = DataBindingUtil.inflate<FragmentMapsBinding>(
            inflater,
            R.layout.fragment_maps,
            container,
            false
        ).apply {
            lifecycleOwner = this@MapsFragment
            viewModel = _viewModel
            onClickGPSFixed = View.OnClickListener {
                _viewModel.moveToCurrentLocation(requireContext(), mLocationProviderClient)
            }
        }

//        _viewModel.fetchNearOrders(requireContext(), mLocationProviderClient)

        setupMap()
        setupClusterer()
        setupSearchAutoComplete()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeState()
        observeEvent(_viewModel)
    }

    override fun onStart() {
        super.onStart()
        _viewModel.getCurrentSignInUser()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        mMap?.let { map ->
            context?.let {
                if (ActivityCompat.checkSelfPermission(
                        it,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    map.isMyLocationEnabled = true
                    _viewModel.moveToCurrentLocation(it, mLocationProviderClient)
                } else {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_CODE_PERMISSION
                    )
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

    private fun observeState() {
        _viewModel.run {
            this.userLocation.nonNullObserve(viewLifecycleOwner) {
                safeLet(it.latitude, it.longitude) { latitude, longitude ->
                    moveToUserLocation(LatLng(latitude, longitude))
                }
            }
        }
    }

    private fun observeEvent(viewModel: MapsViewModel) {
        viewModel.run {
            this.backToLoginCommand.nonNullObserve(viewLifecycleOwner) {
                // _viewModel.signOut()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setupMap() {
        val mapFragment: SupportMapFragment? =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    private fun setupClusterer() {
        mClusterManager = ClusterManager(context, mMap)
        mMap.apply {
            this?.setOnCameraIdleListener(mClusterManager)
            this?.setOnMarkerClickListener(mClusterManager)
        }
        addItems()
    }

    private fun addItems() {

    }

    private fun moveToUserLocation(latLng: LatLng) {
        mMap?.let { map ->
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15f)
            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Your location.")
//                    .icon(BitmapDescriptorFactory.fromBitmap())
            )
            map.moveCamera(cameraUpdate)
            map.animateCamera(cameraUpdate)
        }
    }


    private fun setupSearchAutoComplete() {
        val adapter =
            object : ArrayAdapter<String>(
                requireContext(),
                android.R.layout.select_dialog_item,
                resources.getStringArray(R.array.cities)
            ) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent)
                    (view as TextView).textSize = 14f
                    return view
                }
            }
        binding.searchText.run {
            this.setAdapter(adapter)
            this.threshold = 1
            this.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    this.showDropDown()
                }
            }
        }
    }
}
