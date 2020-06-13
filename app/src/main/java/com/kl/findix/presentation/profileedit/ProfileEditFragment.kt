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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
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
        private const val DEFAULT_PHOTO_NUMBER = -1
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

    private var photoNumber = DEFAULT_PHOTO_NUMBER
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
            setOnClickPhoto1 { chooseWorkPhoto(1) }
            setOnClickPhoto2 { chooseWorkPhoto(2) }
            setOnClickPhoto3 { chooseWorkPhoto(3) }
            setOnClickPhoto4 { chooseWorkPhoto(4) }
            setOnClickPhoto5 { chooseWorkPhoto(5) }
            setOnClickSave {
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
        _viewModel.setWorkPhotos()
        observeState(_viewModel)
        observeEvent(_viewModel)
    }

    private fun chooseWorkPhoto(number: Int) {
        photoNumber = number
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = GALLERY_TYPE_IMAGE
        startActivityForResult(intent, REQUEST_CODE_CHOOOSE_WORK_PHOTO)
    }

    private fun observeEvent(viewModel: ProfileEditViewModel) {
        viewModel.run {
            setWorkPhotosCommand.nonNullObserve(viewLifecycleOwner) {
                context?.let { context ->
                    Glide.with(context)
                        .applyDefaultRequestOptions(
                            RequestOptions.diskCacheStrategyOf(
                                DiskCacheStrategy.NONE
                            )
                        )
                        .load(it.second)
                        .placeholder(R.color.colorBlack_10)
                        .error(R.color.colorBlack_10)
                        .into(getWorkPhotoImageView(it.first))
                }
            }
        }
    }

    private fun observeState(viewModel: ProfileEditViewModel) {
        viewModel.run {
            photos.forEachIndexed { index, photo ->
                photo.nonNullObserve(viewLifecycleOwner) {
                    context?.let { context ->
                        Glide.with(context)
                            .applyDefaultRequestOptions(
                                RequestOptions.diskCacheStrategyOf(
                                    DiskCacheStrategy.NONE
                                )
                            )
                            .load(it.bitmap)
                            .into(getWorkPhotoImageView(index + 1))
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHOOOSE_WORK_PHOTO && resultCode == Activity.RESULT_OK) {
            safeLet(
                data?.data,
                activity?.contentResolver
            ) { uri, contentResolver ->
                if (photoNumber != DEFAULT_PHOTO_NUMBER) {
                    _viewModel.updateWorkPhoto(
                        number = photoNumber,
                        uri = uri,
                        contentResolver = contentResolver
                    )
                }
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
}