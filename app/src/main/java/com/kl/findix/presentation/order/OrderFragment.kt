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
import com.kl.findix.databinding.FragmentOrderBinding
import com.kl.findix.di.ViewModelFactory
import com.kl.findix.navigation.OrderNavigator
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class OrderFragment : Fragment() {

    companion object {
        private const val TAG = "OrderFragment"
    }

    @Inject
    lateinit var navigator: OrderNavigator
    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var _viewModel: OrderViewModel
    private lateinit var binding: FragmentOrderBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewModel = ViewModelProviders.of(this, mViewModelFactory).get(OrderViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = _viewModel
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        observeState(_viewModel)
//        observeEvent(_viewModel)
    }

    private fun observeState(viewModel: OrderViewModel) {
        TODO()
    }

    private fun observeEvent(viewModel: OrderViewModel) {
        TODO()
    }
}