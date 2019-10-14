package com.kl.findix.presentation.login

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
import com.kl.findix.databinding.FragmentSignupBinding
import com.kl.findix.di.ViewModelFactory
import com.kl.findix.presentation.map.MapsActivity
import com.kl.findix.util.nonNullObserve
import com.kl.findix.util.showToast
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_signup.*
import javax.inject.Inject

class SignUpFragment : Fragment() {

    private lateinit var _viewModel: SignUpViewModel
    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var epoxyController: SignUpController
    private val binding: FragmentSignupBinding by lazy {
        DataBindingUtil.inflate<FragmentSignupBinding>(
            layoutInflater,
            R.layout.fragment_signup,
            container,
            false
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewModel = ViewModelProviders.of(this, mViewModelFactory).get(SignUpViewModel::class.java)
        binding.apply {
            lifecycleOwner = this@SignUpFragment
            viewModel = _viewModel
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setController()
        observeEvent(_viewModel)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    private fun setController() {
        epoxyController = SignUpController(
            _viewModel.signInInfo,
            onClickSignUp = {
                _viewModel.emailSignUp()
            }
        ).also {
            binding.recyclerView.setControllerAndBuildModels(it)
        }
    }

    private fun observeEvent(viewModel: SignUpViewModel) {
        viewModel.run {
            this.signUpResult.nonNullObserve(viewLifecycleOwner) { result ->
                context?.let { context ->
                    if (result) {
                        this.emailSignIn()
                    } else{
                        showToast(context, context.getString(R.string.failed_sign_up))
                    }
                }
            }
            this.signInResult.nonNullObserve(viewLifecycleOwner) { result ->
                context?.let { context ->
                    if (result) {
                        val intent = Intent(context, MapsActivity::class.java)
                        startActivity(intent)
                    } else {
                        showToast(context, context.getString(R.string.failed_sign_up))
                    }
                }
            }
        }
    }
}