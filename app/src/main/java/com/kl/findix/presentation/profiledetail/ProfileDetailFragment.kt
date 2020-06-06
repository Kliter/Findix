package com.kl.findix.presentation.profiledetail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.kl.findix.R
import com.kl.findix.databinding.FragmentProfileDetailBinding
import com.kl.findix.util.extension.nonNullObserve
import com.kl.findix.util.extension.viewModelProvider
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ProfileDetailFragment : Fragment() {

    companion object {
        private const val TAG = "ProfileDetailFragment"
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var navController: NavController

    private lateinit var _viewModel: ProfileDetailViewModel
    private lateinit var binding: FragmentProfileDetailBinding

    private val args: ProfileDetailFragmentArgs by navArgs()

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_detail, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = _viewModel
            onClickBack = View.OnClickListener {
                navController.popBackStack()
            }
        }

        _viewModel.fetchUserInfo(args.userId)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeState(_viewModel)
        observeEvent(_viewModel)
    }

    private fun observeState(viewModel: ProfileDetailViewModel) {
        //
    }

    private fun observeEvent(viewModel: ProfileDetailViewModel) {
        viewModel.run {
            this.setProfilePhotoCommand.nonNullObserve(viewLifecycleOwner) { storageReference ->
                Glide.with(requireContext())
                    .load(storageReference)
                    .into(binding.profilePhoto)
            }
        }
    }
}