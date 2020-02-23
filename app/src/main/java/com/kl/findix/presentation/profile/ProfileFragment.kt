package com.kl.findix.presentation.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.kl.findix.R
import com.kl.findix.databinding.FragmentProfileBinding
import com.kl.findix.di.ViewModelFactory
import com.kl.findix.navigation.ProfileNavigator
import com.kl.findix.util.nonNullObserve
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
        }
        lifecycle.addObserver(_viewModel)

        _viewModel.setProfileIcon()
        _viewModel.fetchUserInfo()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeState(_viewModel)
        observeEvent(_viewModel)
    }

    private fun observeEvent(viewModel: ProfileViewModel) {
        viewModel.run {
            setProfileIconCommand.nonNullObserve(viewLifecycleOwner) { storageReference ->
                Glide.with(requireContext())
                    .load(storageReference)
                    .into(binding.profilePhoto)
            }
        }
    }

    private fun observeState(viewModel: ProfileViewModel) {
        viewModel.run {
            profileIconBitmap.nonNullObserve(viewLifecycleOwner) { profileIconBitmap ->
                binding.profileIconSrc = profileIconBitmap
            }
        }
    }
}