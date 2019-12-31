package com.kl.findix.presentation.order

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.kl.findix.R
import com.kl.findix.databinding.FragmentCreateOrderBinding
import com.kl.findix.di.ViewModelFactory
import com.kl.findix.navigation.CreateOrderNavigator
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class CreateOrderFragment : Fragment() {

    companion object {
        private const val TAG = "CreateOrderFragment"
    }

    @Inject
    lateinit var navigator: CreateOrderNavigator
    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var _viewModel: CreateOrderViewModel
    private lateinit var binding: FragmentCreateOrderBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewModel = ViewModelProviders.of(this, mViewModelFactory).get(CreateOrderViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_order, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = _viewModel
        }
        return binding.root
    }
}