package com.kl.findix.presentation.splash

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.kl.findix.R
import com.kl.findix.databinding.FragmentSplashBinding
import com.kl.findix.presentation.login.LoginFragmentDirections
import com.kl.findix.util.extension.nonNullObserve
import com.kl.findix.util.extension.viewModelProvider
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class SplashFragment: Fragment() {

    companion object {
        private const val TAG = "SplashFragment"
    }

    @Inject
    lateinit var mViewMOdelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var navController: NavController

    private lateinit var _viewModel: SplashViewModel
    private lateinit var binding: FragmentSplashBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewModel = viewModelProvider(mViewMOdelFactory)
        binding = DataBindingUtil.inflate<FragmentSplashBinding>(
            inflater,
            R.layout.fragment_splash,
            container,
            false
        ).apply {
            lifecycleOwner = this@SplashFragment
            viewModel = _viewModel
        }
        lifecycle.addObserver(_viewModel)
        _viewModel.isAlreadySignedIn()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeState(_viewModel)
    }

    private fun observeState(viewModel: SplashViewModel) {
        viewModel.isAlreadySignedIn.nonNullObserve(viewLifecycleOwner) {
            if (it) {
                navController.navigate(
                    SplashFragmentDirections.toMaps()
                )
            } else {
                navController.navigate(
                    SplashFragmentDirections.toLogin()
                )
            }
        }
    }
}