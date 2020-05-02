package com.kl.findix.presentation.map

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.AdRequest
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
import com.kl.findix.model.ClusterItem
import com.kl.findix.util.MarkerClusterRenderer
import com.kl.findix.util.REQUEST_CODE_PERMISSION
import com.kl.findix.util.extension.nonNullObserve
import com.kl.findix.util.extension.safeLet
import com.kl.findix.util.extension.viewModelProvider
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MapsFragment : Fragment(), OnMapReadyCallback {

    companion object {
        fun newInstance() = MapsFragment()
        private const val TAG = "MapsFragment"
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var navController: NavController

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
        _viewModel = viewModelProvider(mViewModelFactory)
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
            searchText.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    _viewModel.fetchQueriedCityOrders(binding.searchText.text.toString())
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
        }

        setupMap()
        setupSearchAutoComplete()
        initAd()

        return binding.root
    }

    private fun initAd() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeState()
        observeEvent(_viewModel)
        val navController = findNavController()
        activity?.bottom_navigation_view?.setupWithNavController(navController)
        activity?.bottom_navigation_view?.visibility = View.VISIBLE
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        mMap?.let { map ->
            mClusterManager = ClusterManager(context, map)
            context?.let { context ->
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    mClusterManager.renderer =
                        MarkerClusterRenderer(context, map, mClusterManager)
                    map.isMyLocationEnabled = true
                    map.setOnCameraIdleListener(mClusterManager)
                    map.setOnMarkerClickListener(mClusterManager)
                    _viewModel.moveToCurrentLocation(context, mLocationProviderClient)
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
                safeLet(
                    it.latitude,
                    it.longitude
                ) { latitude, longitude ->
                    moveToUserLocation(LatLng(latitude, longitude))
                }
            }
            this.orders.nonNullObserve(viewLifecycleOwner) { orders ->
                orders.forEach { order ->
                    safeLet(
                        order.userLocation?.latitude,
                        order.userLocation?.longitude,
                        order.title,
                        order.description
                    ) { latitude, longitude, title, description ->
                        val clusterItem =
                            ClusterItem(LatLng(latitude, longitude), title, description)
                        mClusterManager.addItem(clusterItem)
                    }
                }
            }
        }
    }

    private fun observeEvent(viewModel: MapsViewModel) {
        viewModel.run {
            this.backToLoginCommand.nonNullObserve(viewLifecycleOwner) {
                navController.navigate(
                    MapsFragmentDirections.toLogin()
                )
            }
        }
    }

    private fun setupMap() {
        val mapFragment: SupportMapFragment? =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    private fun moveToUserLocation(latLng: LatLng) {
        mMap?.let { map ->
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15f)
            mMap?.clear()
            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Your location.")
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
