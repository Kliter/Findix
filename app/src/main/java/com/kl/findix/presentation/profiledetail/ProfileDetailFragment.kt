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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.kl.findix.R
import com.kl.findix.databinding.FragmentProfileDetailBinding
import com.kl.findix.util.extension.nonNullObserve
import com.kl.findix.util.extension.viewModelProvider
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ProfileDetailFragment : Fragment() {

    companion object {
        private const val TAG = "ProfileDetailFragment"
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

    private lateinit var _viewModel: ProfileDetailViewModel
    private lateinit var binding: FragmentProfileDetailBinding
    private val controller: ProfileDetailController = ProfileDetailController()

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
        binding =
            DataBindingUtil.inflate<FragmentProfileDetailBinding>(
                inflater,
                R.layout.fragment_profile_detail,
                container,
                false
            ).apply {
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
        setController()
        _viewModel.setWorkPhotos()
        observeState(_viewModel)
        observeEvent(_viewModel)
    }

    private fun setController() {
        binding.recyclerView.apply {
            this.layoutManager =
                ProfileDetailLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            this.setController(controller)
        }
    }

    private fun observeState(viewModel: ProfileDetailViewModel) {
        viewModel.run {
            orders.nonNullObserve(viewLifecycleOwner) {
                controller.setData(it)
            }
        }
    }

    private fun observeEvent(viewModel: ProfileDetailViewModel) {
        viewModel.run {
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

    class ProfileDetailLayoutManager(context: Context?, orientation: Int, reverseLayout: Boolean) :
        LinearLayoutManager(context, orientation, reverseLayout) {

        override fun canScrollVertically(): Boolean {
            return false
        }
    }
}
