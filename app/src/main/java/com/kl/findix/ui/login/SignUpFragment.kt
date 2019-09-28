package com.kl.findix.ui.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.kl.findix.R
import com.kl.findix.databinding.FragmentSignupBinding
import com.kl.findix.di.ViewModelFactory
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
    }

    private fun setController() {
        epoxyController = SignUpController(
            onClickSignUp = {
                // Todo
            }
        ).also {
            binding.recyclerView.setControllerAndBuildModels(it)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }
}