package com.kl.findix.presentation.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.airbnb.epoxy.EpoxyController
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.kl.findix.R
import com.kl.findix.databinding.FragmentSettingBinding
import com.kl.findix.presentation.profile.ProfileFragmentDirections
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
                context?.let {
                    AlertDialog.Builder(it)
                        .setTitle(R.string.sign_out_dialog_title)
                        .setMessage(R.string.sign_out_dialog_message)
                        .setPositiveButton(R.string.ok) { _, _ ->
                            _viewModel.signOut()
                            navController.navigate(
                                ProfileFragmentDirections.toLogin()
                            )
                        }
                        .setNegativeButton(R.string.cancel) { _, _ ->
                            // NOP
                        }
                        .show()
                }
            },
            onClickContactUs = {
                // Todo
            },
            onClickPrivacyPolicy = {

            },
            onClickDeleteAccount = {
                context?.let {
                    AlertDialog.Builder(it)
                        .setTitle(R.string.delete_account_dialog_title)
                        .setMessage(R.string.delete_account_dialog_message)
                        .setPositiveButton(R.string.ok) { _, _ ->
                            _viewModel.deleteAccount()
                            navController.navigate(
                                ProfileFragmentDirections.toLogin()
                            )
                        }
                        .setNegativeButton(R.string.cancel) { _, _ ->
                            // NOP
                        }
                        .show()
                }
            },
            onClickLicenses = {
                val intent = Intent(context, OssLicensesMenuActivity::class.java)
                intent.putExtra("title", "Oss Licenses")
                startActivity(intent)
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