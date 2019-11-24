package com.kl.findix.presentation.profile

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.kl.findix.R
import com.kl.findix.databinding.FragmentProfileBinding
import com.kl.findix.di.ViewModelFactory
import com.kl.findix.navigation.ProfileNavigator
import com.kl.findix.util.REQUEST_CODE_CHOOOSE_PROFILE_ICON
import com.kl.findix.util.nonNullObserve
import com.kl.findix.util.safeLet
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

class ProfileFragment : Fragment() {

    companion object {
        private const val TAG = "MapsFragment"
        private const val GALLERY_TYPE_IMAGE = "image/*"
    }

    @Inject
    lateinit var navigator: ProfileNavigator
    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private var epoxyController: ProfileController? = null
    private lateinit var _viewModel: ProfileViewModel
    private val binding: FragmentProfileBinding by lazy {
        DataBindingUtil.inflate<FragmentProfileBinding>(
            layoutInflater,
            R.layout.fragment_profile,
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
            ViewModelProviders.of(this, mViewModelFactory).get(ProfileViewModel::class.java)

        binding.apply {
            lifecycleOwner = this@ProfileFragment
            viewModel = _viewModel
            onClickSave = View.OnClickListener {
                activity?.contentResolver?.let { contentResolver ->
                    _viewModel.saveProfileSettings(contentResolver)
                }
            }
        }

        _viewModel.fetchUserInfo()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setController()
        observeState(_viewModel)
    }

    private fun observeState(viewModel: ProfileViewModel) {
        viewModel.run {
            user.nonNullObserve(viewLifecycleOwner) { user ->
                epoxyController?.user = user
                epoxyController?.requestModelBuild()
            }
            profilePhotoBitmap.nonNullObserve(viewLifecycleOwner) { profilePhotoBitmap ->
                epoxyController?.profilePhotoSrc = profilePhotoBitmap
                epoxyController?.requestModelBuild()
            }
        }

    }

    private fun setController() {
        context?.let {
            epoxyController = ProfileController(
                onClickUserIcon = {
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                    intent.addCategory(Intent.CATEGORY_OPENABLE)
                    intent.type = GALLERY_TYPE_IMAGE
                    startActivityForResult(intent, REQUEST_CODE_CHOOOSE_PROFILE_ICON)
                }
            ).also {
                epoxyController?.user = _viewModel._user
                binding.recyclerView.setControllerAndBuildModels(it)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHOOOSE_PROFILE_ICON && resultCode == RESULT_OK) {
            safeLet(data?.data, activity?.contentResolver) { uri, contentResolver ->
                _viewModel.updateProfilePhoto(uri, contentResolver)
            }
        }
    }
}