package com.kl.findix.presentation.order

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kl.findix.R
import com.kl.findix.databinding.FragmentCreateOrderBinding
import com.kl.findix.di.ViewModelFactory
import com.kl.findix.navigation.CreateOrderNavigator
import com.kl.findix.util.nonNullObserve
import com.kl.findix.util.safeLet
import com.kl.findix.util.showToast
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
    private lateinit var mLocationProviderClient: FusedLocationProviderClient


    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(activity as Activity)
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
            onClickBack = View.OnClickListener {
                navigator.toPrev()
            }
            onClickSave = View.OnClickListener {
                val isFilledTitle = binding.textInputLayoutTitle.editText?.text?.isNotBlank()
                val isFilledDescription =
                    binding.textInputLayoutDescription.editText?.text?.isNotBlank()
                safeLet(isFilledTitle, isFilledDescription) { isFilledTitle, isFilledDescription ->
                    createOrderIfEnable(isFilledTitle, isFilledDescription)
                }
            }
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeEvent(_viewModel)
    }

    private fun observeEvent(viewModel: CreateOrderViewModel) {
        viewModel.run {
            showToastCommand.nonNullObserve(viewLifecycleOwner) {
                context?.let { context ->
                    showToast(context, getString(it))
                }
            }
            succeedCreateOrderCommand.nonNullObserve(viewLifecycleOwner) {
                if (it) {
                    context?.let { context ->
                        showToast(context, getString(R.string.succeed_create_order))
                    }
                    navigator.toPrev()
                }
            }
        }
    }

    private fun createOrderIfEnable(isFilledTitle: Boolean, isFilledDescription: Boolean) {
        if (!isFilledTitle) {
            binding.textInputLayoutTitle.apply {
                this.isErrorEnabled = true
                this.error = getString(R.string.error_title_is_not_filled)
            }
        } else if (!isFilledDescription) {
            binding.textInputLayoutDescription.apply {
                this.isErrorEnabled = true
                this.error = getString(R.string.error_description_is_not_filled)
            }
        } else {
            context?.let { context ->
                _viewModel.createOrder(context, mLocationProviderClient)
            }
        }
    }
}