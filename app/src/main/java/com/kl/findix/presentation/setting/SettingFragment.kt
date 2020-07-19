package com.kl.findix.presentation.setting

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.airbnb.epoxy.EpoxyController
import com.kl.findix.R
import com.kl.findix.databinding.FragmentSettingBinding
import com.kl.findix.util.extension.viewModelProvider
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class SettingFragment : Fragment() {

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var navController: NavController

    private lateinit var _viewModel: SettingViewModel
    private lateinit var binding: FragmentSettingBinding
    private var controller: SettingController? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewModel = viewModelProvider(mViewModelFactory)
        binding = DataBindingUtil.inflate<FragmentSettingBinding>(
            layoutInflater,
            R.layout.fragment_setting,
            container,
            false
        ).apply {
            lifecycleOwner = this@SettingFragment
            viewModel = _viewModel
        }
        lifecycle.addObserver(_viewModel)


        setupController()
        observeState(_viewModel)
        observeEvent(_viewModel)

        return binding.root
    }

    private fun setupController() {
        controller = SettingController(
            onClickSignOut = {

            },
            onClickContactUs = {

            },
            onClickPrivacyPolicy = {

            },
            onClickDeleteAccount = {

            },
            onClickLicences = {
                // Todo
            }
        )
        binding.recyclerView.apply {
            this.setController(controller as EpoxyController)
        }
        controller?.requestModelBuild()
    }

    private fun observeState(viewModel: SettingViewModel) {
        // Todo
    }

    private fun observeEvent(viewModel: SettingViewModel) {
        // Todo
    }
}