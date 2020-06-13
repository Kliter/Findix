package com.kl.findix.presentation.photo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.kl.findix.R
import com.kl.findix.databinding.FragmentPhotoBinding
import com.kl.findix.di.FindixViewModelFactory
import com.kl.findix.util.extension.nonNullObserve
import com.kl.findix.util.extension.setBitmapSrc
import com.kl.findix.util.extension.viewModelProvider
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_photo.*
import javax.inject.Inject

class PhotoFragment : Fragment() {

    @Inject
    lateinit var mViewModelFactory: FindixViewModelFactory
    @Inject
    lateinit var navController: NavController

    private lateinit var _viewModel: PhotoViewModel
    private lateinit var binding: FragmentPhotoBinding

    private val args: PhotoFragmentArgs by navArgs()

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

        binding = DataBindingUtil.inflate<FragmentPhotoBinding>(
            layoutInflater,
            R.layout.fragment_photo,
            container,
            false
        ).apply {
            lifecycleOwner = this@PhotoFragment
            viewModel = _viewModel
        }
        lifecycle.addObserver(_viewModel)

        _viewModel.showWorkPhoto(args.index)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeEvent(_viewModel)
    }

    private fun observeEvent(viewModel: PhotoViewModel) {
        viewModel.run {
            this.showWorkPhotoCommand.nonNullObserve(viewLifecycleOwner) {
                context?.let { context ->
                    Glide.with(context)
                        .applyDefaultRequestOptions(
                            RequestOptions.diskCacheStrategyOf(
                                DiskCacheStrategy.NONE
                            )
                        )
                        .load(it)
                        .placeholder(R.color.colorBlack_10)
                        .into(image_photo)
                }
            }
        }
    }
}