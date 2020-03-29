package com.kl.findix.presentation.profile

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyController
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.google.android.material.snackbar.Snackbar
import com.kl.findix.R
import com.kl.findix.databinding.FragmentProfileBinding
import com.kl.findix.di.ViewModelFactory
import com.kl.findix.navigation.ProfileNavigator
import com.kl.findix.util.extension.nonNullObserve
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ProfileFragment : Fragment() {

    companion object {
        private const val TAG = "MapsFragment"
    }

    @Inject
    lateinit var navigator: ProfileNavigator
    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

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
        _viewModel =
            ViewModelProviders.of(this, mViewModelFactory).get(ProfileViewModel::class.java)

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
                navigator.toProfileEdit()
            }
        }
        lifecycle.addObserver(_viewModel)

        _viewModel.setProfileIcon()
        _viewModel.fetchUserInfo()
        _viewModel.fetchOwnOrder()

        initAd()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setController()
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
            setProfileIconCommand.nonNullObserve(viewLifecycleOwner) { storageReference ->
                Glide.with(requireContext())
                    .load(storageReference)
                    .into(binding.profilePhoto)
            }
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

    class ProfileLayoutManager(context: Context?, orientation: Int, reverseLayout: Boolean) :
        LinearLayoutManager(context, orientation, reverseLayout) {

        override fun canScrollVertically(): Boolean {
            return false
        }
    }
}