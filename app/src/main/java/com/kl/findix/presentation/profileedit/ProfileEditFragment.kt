package com.kl.findix.presentation.profileedit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.kl.findix.R
import com.kl.findix.databinding.FragmentProfileEditBinding
import com.kl.findix.util.GALLERY_TYPE_IMAGE
import com.kl.findix.util.REQUEST_CODE_CHOOOSE_WORK_PHOTO
import com.kl.findix.util.extension.nonNullObserve
import com.kl.findix.util.extension.safeLet
import com.kl.findix.util.extension.viewModelProvider
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

class ProfileEditFragment : Fragment() {

    companion object {
        private const val TAG = "ProfileEditFragment"
        private const val KEY_PHOTO_NUMBER = "key_photo_number"
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var navController: NavController

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
        _viewModel = viewModelProvider(mViewModelFactory)

        binding.apply {
            lifecycleOwner = this@ProfileEditFragment
            viewModel = _viewModel
            swipeRefresh.setOnRefreshListener {
                _viewModel.fetchUserInfo()
            }
            setOnClickPhoto1 { createIntent(1) }
            setOnClickPhoto2 { createIntent(2) }
            setOnClickPhoto3 { createIntent(3) }
            setOnClickPhoto4 { createIntent(4) }
            setOnClickPhoto5 { createIntent(5) }
            setOnClickSave{
                _viewModel.saveProfile()
                _viewModel.savePhoto()
            }
            setOnClickSignOut {
                context?.let {
                    AlertDialog.Builder(it)
                        .setTitle(R.string.sign_out_dialog_title)
                        .setMessage(R.string.sign_out_dialog_message)
                        .setPositiveButton(R.string.ok) { _, _ ->
                            _viewModel.signOut()
                            navController.navigate(
                                ProfileEditFragmentDirections.toLogin()
                            )
                        }
                        .show()
                }
            }
            this.toolbar.setNavigationOnClickListener {
                navController.popBackStack()
            }
        }
        lifecycle.addObserver(_viewModel)

        _viewModel.fetchUserInfo()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeState(_viewModel)
        observeEvent(_viewModel)
    }

    private fun createIntent(number: Int) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = GALLERY_TYPE_IMAGE
        intent.putExtra(KEY_PHOTO_NUMBER, number)
        startActivityForResult(intent, REQUEST_CODE_CHOOOSE_WORK_PHOTO)
    }

    private fun observeEvent(viewModel: ProfileEditViewModel) {
        viewModel.run {
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
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHOOOSE_WORK_PHOTO && resultCode == Activity.RESULT_OK) {
            safeLet(
                data?.data,
                activity?.contentResolver
            ) { uri, contentResolver ->
                data?.getIntExtra(KEY_PHOTO_NUMBER, 0)?.let { number ->
                    _viewModel.updateWorkPhoto(
                        number = number,
                        uri = uri,
                        contentResolver = contentResolver
                    )
                }
            }
        }
    }
}