package com.kl.findix.presentation.createorder

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kl.findix.R
import com.kl.findix.databinding.FragmentCreateOrderBinding
import com.kl.findix.di.ViewModelFactory
import com.kl.findix.navigation.CreateOrderNavigator
import com.kl.findix.util.GALLERY_TYPE_IMAGE
import com.kl.findix.util.REQUEST_CODE_CHOOOSE_PROFILE_ICON
import com.kl.findix.util.extension.nonNullObserve
import com.kl.findix.util.extension.safeLet
import com.kl.findix.util.extension.showToast
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
        _viewModel =
            ViewModelProviders.of(this, mViewModelFactory).get(CreateOrderViewModel::class.java)

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_create_order, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = _viewModel
            onClickSetPhoto = View.OnClickListener {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = GALLERY_TYPE_IMAGE
                startActivityForResult(intent, REQUEST_CODE_CHOOOSE_PROFILE_ICON)
            }
            onClickBack = View.OnClickListener {
                navigator.toPrev()
            }
            onClickSave = View.OnClickListener {
                val isTitleFilled = binding.textInputLayoutTitle.editText?.text?.isNotBlank()
                val isDescriptionFilled =
                    binding.textInputLayoutDescription.editText?.text?.isNotBlank()
                safeLet(
                    isTitleFilled,
                    isDescriptionFilled
                ) { isTitleFilled, isDescriptionFilled ->
                    createOrderIfEnable(isTitleFilled, isDescriptionFilled)
                }
            }
        }

        _viewModel.resetOrderInfo()
        setupSpinner()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeEvent(_viewModel)
        observeState(_viewModel)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHOOOSE_PROFILE_ICON && resultCode == Activity.RESULT_OK) {
            safeLet(
                data?.data,
                activity?.contentResolver
            ) { uri, contentResolver ->
                _viewModel.updateOrderPhoto(uri, contentResolver)
            }
        }
    }

    private fun observeState(viewModel: CreateOrderViewModel) {
        viewModel.run {
            this.orderPhotoBitmap.nonNullObserve(viewLifecycleOwner) { orderPhotoBitmap ->
                binding.orderPhotoSrc = orderPhotoBitmap
            }
        }
    }


    private fun observeEvent(viewModel: CreateOrderViewModel) {
        viewModel.run {
            setOrderPhotoCommand.nonNullObserve(viewLifecycleOwner) { storageReference ->
                Glide.with(requireContext())
                    .load(storageReference)
                    .into(binding.image)
            }
            showToastCommand.nonNullObserve(viewLifecycleOwner) {
                context?.let { context ->
                    showToast(
                        context,
                        getString(it)
                    )
                }
            }
            succeedCreateOrderCommand.nonNullObserve(viewLifecycleOwner) {
                if (it) {
                    context?.let { context ->
                        showToast(
                            context,
                            getString(R.string.succeed_create_order)
                        )
                    }
                    navigator.toPrev()
                }
            }
        }
    }

    private fun createOrderIfEnable(
        isTitleFilled: Boolean,
        isDescriptionFilled: Boolean
    ) {
        if (!isTitleFilled) {
            binding.textInputLayoutTitle.apply {
                this.isErrorEnabled = true
                this.error = getString(R.string.error_title_is_not_filled)
            }
        } else if (!isDescriptionFilled) {
            binding.textInputLayoutDescription.apply {
                this.isErrorEnabled = true
                this.error = getString(R.string.error_description_is_not_filled)
            }
        } else {
            safeLet(
                context,
                activity
            ) { context, activity ->
                _viewModel.createOrder(context, mLocationProviderClient, activity.contentResolver)
            }
        }
    }

    private fun setupSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.cities,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }
}