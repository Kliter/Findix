package com.kl.findix.presentation.signup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.kl.findix.R
import com.kl.findix.databinding.FragmentSignupBinding
import com.kl.findix.di.ViewModelFactory
import com.kl.findix.navigation.SignUpNavigator
import com.kl.findix.util.extension.nonNullObserve
import com.kl.findix.util.extension.showToast
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class SignUpFragment : Fragment() {

    private lateinit var _viewModel: SignUpViewModel
    @Inject
    lateinit var mViewModelFactory: ViewModelFactory
    @Inject
    lateinit var navigator: SignUpNavigator

    private lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewModel = ViewModelProviders.of(this, mViewModelFactory).get(SignUpViewModel::class.java)

        binding = DataBindingUtil.inflate<FragmentSignupBinding>(
            inflater,
            R.layout.fragment_signup,
            container,
            false
        ).apply {
            lifecycleOwner = this@SignUpFragment
            viewModel = _viewModel
            onClickSignUp = View.OnClickListener {
                _viewModel.signUpWithEmail()
            }
            onClickBack = View.OnClickListener {
                navigator.toPrev()
            }
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeEvent(_viewModel)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    private fun observeEvent(viewModel: SignUpViewModel) {
        viewModel.run {
            this.signUpResult.nonNullObserve(viewLifecycleOwner) { result ->
                context?.let { context ->
                    if (result) {
                        this.signInWithEmail()
                    } else {
                        showToast(
                            context,
                            context.getString(R.string.failed_sign_up)
                        )
                    }
                }
            }
            this.signInResult.nonNullObserve(viewLifecycleOwner) { result ->
                context?.let { context ->
                    if (result) {
                        navigator.toMaps()
                    } else {
                        showToast(
                            context,
                            context.getString(R.string.failed_sign_up)
                        )
                    }
                }
            }
        }
    }
}