package com.kl.findix.ui.map

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
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
import com.google.android.material.navigation.NavigationView
import com.kl.findix.ui.list.ListActivity
import com.kl.findix.ui.login.LoginActivity
import com.kl.findix.ui.message.MessageActivity
import com.kl.findix.ui.profile.ProfileActivity
import com.kl.findix.util.setupBottomNavigationView
import com.kl.findix.viewmodel.LoginViewModel
import com.kl.findix.viewmodel.ViewModelFactory
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
