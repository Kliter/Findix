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
import com.kl.findix.R
import com.kl.findix.databinding.FragmentPhotoBinding
import com.kl.findix.di.FindixViewModelFactory
import com.kl.findix.util.extension.setBitmapSrc
import com.kl.findix.util.extension.viewModelProvider
import dagger.android.support.AndroidSupportInjection
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

        binding.imagePhoto.setBitmapSrc(args.photo.bitmap)

        return binding.root
    }
}