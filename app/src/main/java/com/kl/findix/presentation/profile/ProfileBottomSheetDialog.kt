package com.kl.findix.presentation.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kl.findix.R
import com.kl.findix.databinding.DialogProfileBottomSheetBinding
import com.kl.findix.di.ViewModelFactory
import com.kl.findix.util.extension.viewModelProvider
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ProfileBottomSheetDialog(
    private val orderId: String?
) : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(orderId: String): ProfileBottomSheetDialog {
            return ProfileBottomSheetDialog(orderId)
        }
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    private lateinit var _viewModel: ProfileViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
        _viewModel = viewModelProvider(mViewModelFactory)
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
            orderId?.let {
                _viewModel.showDeleteOrderConfirm(it)
            }
            dismissAllowingStateLoss()
        }
        binding.btnCancel.setOnClickListener {
            dismissAllowingStateLoss()
        }
        return binding.root
    }
}