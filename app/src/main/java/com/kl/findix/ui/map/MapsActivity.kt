package com.kl.findix.ui.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import dagger.android.AndroidInjection
import com.kl.findix.R
import kotlinx.android.synthetic.main.activity_maps.*
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.navigation.NavigationView
import com.kl.findix.ui.list.ListActivity
import com.kl.findix.ui.login.LoginActivity
import com.kl.findix.ui.message.MessageActivity
import com.kl.findix.ui.profile.ProfileActivity
import com.kl.findix.util.REQUEST_CODE_PERMISSION
import com.kl.findix.util.setupBottomNavigationView
import com.kl.findix.di.ViewModelFactory
import kotlinx.android.synthetic.main.layout_toolbar.*
import javax.inject.Inject

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    companion object {
        private const val TAG = "MapsActivity"
        fun newInstance(context: Context) = Intent(context, MapsActivity::class.java)
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private var mMap: GoogleMap? = null
    private lateinit var mMapsViewModel: MapsViewModel

    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        mMapsViewModel = ViewModelProviders.of(this, mViewModelFactory).get(MapsViewModel::class.java)

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

        setupWidgets()
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
        mMapsViewModel.signOut()
        if (mMapsViewModel.getCurrentSignInUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        bottom_navigation_view.selectedItemId = R.id.action_map
        setupBottomNavigationView(this, bottom_navigation_view)
    }

    private fun setupWidgets() {
        setupToolbar()
        setupDrawer()
        setupMap()
    }

    private fun setupDrawer() {
        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.open,
            R.string.close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        navigation_view.setNavigationItemSelectedListener(this)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun setupMap() {
        val mapFragment: SupportMapFragment = map as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_map, menu)

        if (menu != null) {
            val searchView = menu.findItem(R.id.search_view).actionView as SearchView
            searchView.setOnQueryTextListener(this)
        }
        return true
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        mMap?.let {
            val sydney = LatLng(-34.0, 151.0)// Tmp implementation.
            it.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
            it.moveCamera(CameraUpdateFactory.newLatLng(sydney))

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                it.isMyLocationEnabled = true
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                finish()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val intent = Intent()
        when (item.itemId) {
            R.id.action_profile -> {
                intent.setClass(this, ProfileActivity::class.java)
            }
            R.id.action_message -> {
                intent.setClass(this, MessageActivity::class.java)
            }
            R.id.action_list -> {
                intent.setClass(this, ListActivity::class.java)
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        startActivity(intent)

        return true
    }

    override fun onQueryTextSubmit(text: String?): Boolean {
        Log.d(TAG, "text is submitted")
        return true
    }

    override fun onQueryTextChange(text: String?): Boolean {
        return false
    }
}
