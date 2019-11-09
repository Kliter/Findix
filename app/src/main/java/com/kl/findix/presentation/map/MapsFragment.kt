package com.kl.findix.presentation.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
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
import com.kl.findix.R
import com.kl.findix.databinding.FragmentMapsBinding
import com.kl.findix.di.ViewModelFactory
import com.kl.findix.navigation.MapsNavigator
import com.kl.findix.presentation.login.LoginActivity
import com.kl.findix.util.REQUEST_CODE_PERMISSION
import com.kl.findix.util.nonNullObserve
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_maps.*
import javax.inject.Inject

class MapsFragment : Fragment(), OnMapReadyCallback, SearchView.OnQueryTextListener {

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
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
        }
        setupMap()

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

    private fun observeState() {
        //Todo
    }

    private fun observeEvent(viewModel: MapsViewModel) {
        viewModel.backToLoginCommand.nonNullObserve(viewLifecycleOwner) {
            // _viewModel.signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
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

    private fun setupMap() {
        val mapFragment: SupportMapFragment? = map as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }
}