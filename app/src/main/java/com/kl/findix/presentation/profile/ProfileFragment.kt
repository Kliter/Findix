package com.kl.findix.presentation.profile

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.ads.AdRequest
import com.google.android.material.snackbar.Snackbar
import com.kl.findix.R
import com.kl.findix.databinding.FragmentProfileBinding
import com.kl.findix.util.extension.nonNullObserve
import com.kl.findix.util.extension.viewModelProvider
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ProfileFragment : Fragment() {

    companion object {
        private const val TAG = "MapsFragment"
        private const val PHOTO_INDEX_1 = 1
        private const val PHOTO_INDEX_2 = 2
        private const val PHOTO_INDEX_3 = 3
        private const val PHOTO_INDEX_4 = 4
        private const val PHOTO_INDEX_5 = 5
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var navController: NavController

    private lateinit var _viewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding
    private var controller: ProfileController? = null

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

        binding = DataBindingUtil.inflate<FragmentProfileBinding>(
            layoutInflater,
            R.layout.fragment_profile,
            container,
            false
        ).apply {
            lifecycleOwner = this@ProfileFragment
            viewModel = _viewModel
            toolbar.setTitle(R.string.action_profile)
            this.setOnClickEdit {
                navController.navigate(
                    ProfileFragmentDirections.toProfileEdit()
                )
            }
            this.setOnClickPhoto1 {
                navController.navigate(
                    ProfileFragmentDirections.toPhoto(
                        PHOTO_INDEX_1
                    )
                )
            }
            this.setOnClickPhoto2 {
                navController.navigate(
                    ProfileFragmentDirections.toPhoto(
                        PHOTO_INDEX_2
                    )
                )
            }
            this.setOnClickPhoto3 {
                navController.navigate(
                    ProfileFragmentDirections.toPhoto(
                        PHOTO_INDEX_3
                    )
                )
            }
            this.setOnClickPhoto4 {
                navController.navigate(
                    ProfileFragmentDirections.toPhoto(
                        PHOTO_INDEX_4
                    )
                )
            }
            this.setOnClickPhoto5 {
                navController.navigate(
                    ProfileFragmentDirections.toPhoto(
                        PHOTO_INDEX_5
                    )
                )
            }
        }
        lifecycle.addObserver(_viewModel)

        _viewModel.fetchUserInfo()
        _viewModel.fetchOwnOrder()

        initAd()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setController()
        _viewModel.setWorkPhotos()
        observeState(_viewModel)
        observeEvent(_viewModel)
    }

    private fun initAd() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    private fun setController() {
        controller = ProfileController(
            onClickMenu = { orderId ->
                val dialog = ProfileBottomSheetDialog.newInstance(orderId)
                dialog.show(childFragmentManager, "Delete")
            }
        )
        binding.recyclerView.let {
            it.setController(controller as EpoxyController)
            it.layoutManager = ProfileLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun observeEvent(viewModel: ProfileViewModel) {
        viewModel.run {
            showDeleteOrderConfirmDialogCommand.nonNullObserve(viewLifecycleOwner) { orderId ->
                AlertDialog.Builder(context)
                    .setTitle("")
                    .setMessage(getString(R.string.confirm_delete_order))
                    .setPositiveButton(getString(R.string.ok)) { _, _ ->
                        viewModel.deleteOrder(orderId)
                    }.show()
            }
            showSnackBarCommand.nonNullObserve(viewLifecycleOwner) { resId ->
                Snackbar.make(binding.container, getString(resId), Snackbar.LENGTH_LONG).show()
            }
            setWorkPhotosCommand.nonNullObserve(viewLifecycleOwner) {
                context?.let { context ->
                    val workPhotoImageView = getWorkPhotoImageView(it.first)
                    Glide.with(context)
                        .applyDefaultRequestOptions(
                            RequestOptions.diskCacheStrategyOf(
                                DiskCacheStrategy.NONE
                            )
                        )
                        .load(it.second)
                        .placeholder(R.color.colorBlack_10)
                        .into(workPhotoImageView)
                }
            }
        }
    }

    private fun observeState(viewModel: ProfileViewModel) {
        viewModel.run {
            profileIconBitmap.nonNullObserve(viewLifecycleOwner) { profileIconBitmap ->
                binding.profileIconSrc = profileIconBitmap
            }
            orders.nonNullObserve(viewLifecycleOwner) { orders ->
                controller?.setData(orders)
            }
        }
    }

    private fun getWorkPhotoImageView(index: Int) = when (index) {
        PHOTO_INDEX_1 -> {
            binding.photo1
        }
        PHOTO_INDEX_2 -> {
            binding.photo2
        }
        PHOTO_INDEX_3 -> {
            binding.photo3
        }
        PHOTO_INDEX_4 -> {
            binding.photo4
        }
        PHOTO_INDEX_5 -> {
            binding.photo5
        }
        else -> {
            throw IndexOutOfBoundsException("")
        }
    }

    class ProfileLayoutManager(context: Context?, orientation: Int, reverseLayout: Boolean) :
        LinearLayoutManager(context, orientation, reverseLayout) {

        override fun canScrollVertically(): Boolean {
            return false
        }
    }
}