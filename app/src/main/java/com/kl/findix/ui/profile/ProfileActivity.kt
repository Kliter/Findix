package com.kl.findix.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kl.findix.R
import com.kl.findix.databinding.ActivityProfileBinding
import com.kl.findix.util.setupBottomNavigationView
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_profile.bottom_navigation_view

class ProfileActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ProfileActivity"
    }

    private lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityProfileBinding>(this, R.layout.activity_profile)

        database = FirebaseDatabase.getInstance()
        myRef = database.getReference("User")

    }

    override fun onResume() {
        super.onResume()
        bottom_navigation_view.selectedItemId = R.id.action_profile
        setupBottomNavigationView(this, bottom_navigation_view)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_save -> {
                saveProfileSettings()
            }
        }
        return true
    }

    private fun saveProfileSettings() {
//        myRef.child("users").child(userId).setValue(user)
    }
}
