package com.kl.findix.presentation.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kl.findix.R
import com.kl.findix.databinding.DialogProfileBottomSheetBinding
import com.kl.findix.di.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ProfileBottomSheetDialog : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(): ProfileBottomSheetDialog {
            return ProfileBottomSheetDialog()
        }
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var _viewModel: ProfileViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
        _viewModel =
            ViewModelProviders.of(this, mViewModelFactory).get(ProfileViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_ShareDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogProfileBottomSheetBinding.inflate(inflater)
        binding.viewModel = _viewModel
        binding.btnDeleteOrder.setOnClickListener {
            _viewModel.showDeleteOrderConfirm()
            dismissAllowingStateLoss()
        }
        binding.btnCancel.setOnClickListener {
            dismissAllowingStateLoss()
        }
        return binding.root
    }
}