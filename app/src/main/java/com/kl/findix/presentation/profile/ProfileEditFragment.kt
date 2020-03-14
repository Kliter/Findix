package com.kl.findix.presentation.profile

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.kl.findix.R
import com.kl.findix.databinding.FragmentProfileEditBinding
import com.kl.findix.di.ViewModelFactory
import com.kl.findix.navigation.ProfileEditNavigator
import com.kl.findix.presentation.login.LoginActivity
import com.kl.findix.util.GALLERY_TYPE_IMAGE
import com.kl.findix.util.REQUEST_CODE_CHOOOSE_PROFILE_ICON
import com.kl.findix.util.nonNullObserve
import com.kl.findix.util.safeLet
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

class ProfileEditFragment : Fragment() {

    companion object {
        private const val TAG = "ProfileEditFragment"
    }

    @Inject
    lateinit var navigator: ProfileEditNavigator
    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var _viewModel: ProfileEditViewModel
    private val binding: FragmentProfileEditBinding by lazy {
        DataBindingUtil.inflate<FragmentProfileEditBinding>(
            layoutInflater,
            R.layout.fragment_profile_edit,
            container,
            false
        )
    }

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
            ViewModelProviders.of(this, mViewModelFactory).get(ProfileEditViewModel::class.java)

        binding.apply {
            lifecycleOwner = this@ProfileEditFragment
            viewModel = _viewModel
            swipeRefresh.setOnRefreshListener {
                _viewModel.fetchUserInfo()
            }
            onClickSave = View.OnClickListener {
                activity?.contentResolver?.let { contentResolver ->
                    _viewModel.saveProfile(contentResolver)
                }
            }
            onClickUserIcon = View.OnClickListener {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = GALLERY_TYPE_IMAGE
                startActivityForResult(intent, REQUEST_CODE_CHOOOSE_PROFILE_ICON)
            }
            onClickSignOut = View.OnClickListener {
                AlertDialog.Builder(context)
                    .setTitle(R.string.sign_out_dialog_title)
                    .setMessage(R.string.sign_out_dialog_message)
                    .setPositiveButton(R.string.ok) { _, _ ->
                        _viewModel.signOut()
                        val intent = Intent(context, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    .show()
            }

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

    private fun observeEvent(viewModel: ProfileEditViewModel) {
        viewModel.run {
            setProfileIconCommand.nonNullObserve(viewLifecycleOwner) { storageReference ->
                Glide.with(requireContext())
                    .load(storageReference)
                    .into(binding.profileIcon)
            }
            hideRefreshCommand.nonNullObserve(viewLifecycleOwner) {
                if (it) {
                    if (binding.swipeRefresh.isRefreshing) {
                        binding.swipeRefresh.isRefreshing = false
                    }
                }
            }
        }
    }

    private fun observeState(viewModel: ProfileEditViewModel) {
        viewModel.run {
            profileIconBitmap.nonNullObserve(viewLifecycleOwner) { profileIconBitmap ->
                binding.profileIconSrc = profileIconBitmap
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHOOOSE_PROFILE_ICON && resultCode == Activity.RESULT_OK) {
            safeLet(data?.data, activity?.contentResolver) { uri, contentResolver ->
                _viewModel.updateProfilePhoto(uri, contentResolver)
            }
        }
    }
}