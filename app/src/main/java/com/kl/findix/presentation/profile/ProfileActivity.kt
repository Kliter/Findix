package com.kl.findix.presentation.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kl.findix.R
import com.kl.findix.databinding.ActivityProfileBinding
import com.kl.findix.util.setupBottomNavigationView
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_profile.bottom_navigation_view
import javax.inject.Inject

class ProfileActivity : AppCompatActivity(), HasSupportFragmentInjector {

    companion object {
        private const val TAG = "ProfileActivity"
    }

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>
    private val binding: ActivityProfileBinding by lazy {
        DataBindingUtil.setContentView<ActivityProfileBinding>(this, R.layout.activity_profile)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNavigationView.selectedItemId = R.id.action_profile
        setupBottomNavigationView(this, bottom_navigation_view)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector
}
