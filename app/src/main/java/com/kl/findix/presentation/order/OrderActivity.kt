package com.kl.findix.presentation.order

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.kl.findix.R
import com.kl.findix.databinding.ActivityOrderBinding
import com.kl.findix.di.ViewModelFactory
import com.kl.findix.util.setupBottomNavigationView
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_order.*
import javax.inject.Inject

class OrderActivity : AppCompatActivity(), HasSupportFragmentInjector {

    companion object {
        private const val TAG = "OrderActivity"
        fun newInstance(context: Context) = Intent(context, OrderActivity::class.java)
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var _viewModel: OrderViewModel
    private var binding: ActivityOrderBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        _viewModel = ViewModelProviders.of(this, mViewModelFactory).get(OrderViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order)
        binding?.apply {
            lifecycleOwner = this@OrderActivity
        }
    }

    override fun onResume() {
        super.onResume()
        bottom_navigation_view.selectedItemId = R.id.action_order
        setupBottomNavigationView(this, bottom_navigation_view)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector
}
