package com.kl.findix.presentation.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.kl.findix.R
import com.kl.findix.databinding.ActivityLoginBinding
import com.kl.findix.di.ViewModelFactory
import com.kl.findix.presentation.map.MapsActivity
import com.kl.findix.util.nonNullObserve
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), HasSupportFragmentInjector {

    companion object {
        private const val TAG = "LoginActivity"
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var _viewModel: LoginViewModel
    private val binding: ActivityLoginBinding by lazy {
        DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        _viewModel = ViewModelProviders.of(this, mViewModelFactory).get(LoginViewModel::class.java)
        binding.apply {
            viewModel = _viewModel
        }

//        observeState(_viewModel)
    }

    override fun onStart() {
        super.onStart()
        _viewModel.isSignedIn()
    }

    private fun observeState(viewModel: LoginViewModel) {
        viewModel.run {
            this.signInResult.nonNullObserve(this@LoginActivity) {
                if (it) {
                    val intent = Intent(this@LoginActivity, MapsActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector
}
