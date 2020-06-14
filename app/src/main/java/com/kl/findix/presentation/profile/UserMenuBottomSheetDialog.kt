package com.kl.findix.presentation.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kl.findix.R
import com.kl.findix.databinding.DialogProfileBottomSheetBinding
import com.kl.findix.databinding.DialogUserMenuBottomSheetBinding
import com.kl.findix.util.extension.parentViewModelProvider
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class UserMenuBottomSheetDialog : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(): UserMenuBottomSheetDialog = UserMenuBottomSheetDialog()
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    private lateinit var _viewModel: ProfileViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
        _viewModel = parentViewModelProvider(mViewModelFactory)
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
        val binding = DialogUserMenuBottomSheetBinding.inflate(inflater)
        binding.viewModel = _viewModel
        binding.btnSignOut.setOnClickListener {
            _viewModel.showSignOutDialog()
            dismissAllowingStateLoss()
        }
        binding.btnCancel.setOnClickListener {
            dismissAllowingStateLoss()
        }
        return binding.root
    }
}