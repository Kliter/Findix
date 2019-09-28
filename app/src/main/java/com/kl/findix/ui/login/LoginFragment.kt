package com.kl.findix.ui.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.kl.findix.R
import com.kl.findix.databinding.FragmentLoginBinding
import com.kl.findix.di.ViewModelFactory
import com.kl.findix.navigation.LoginNavigator
import com.kl.findix.ui.map.MapsActivity
import com.kl.findix.util.REQUEST_CODE_SIGN_IN
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class LoginFragment : Fragment(), View.OnClickListener {

    companion object {
        private const val TAG = "LoginFragment"
    }

    private lateinit var _viewModel: LoginViewModel
    @Inject
    lateinit var mViewModelFactory: ViewModelFactory
    @Inject
    lateinit var mGoogleSignInClient: GoogleSignInClient
    @Inject
    lateinit var navigator: LoginNavigator
    private val binding: FragmentLoginBinding by lazy {
        DataBindingUtil.inflate<FragmentLoginBinding>(
            layoutInflater,
            R.layout.fragment_login,
            container,
            false
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewModel = ViewModelProviders.of(this, mViewModelFactory).get(LoginViewModel::class.java)
        binding.apply {
            lifecycleOwner = this@LoginFragment
            viewModel = _viewModel
        }
        binding.onClickGoogleSignInListener = this
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_google_sign_in -> {
                googleSignIn()
                _viewModel.isSignedIn()
            }
            R.id.btn_email_sign_in -> {
                Log.d(TAG, "Pressed EmailSignIn Button.")
            }
            R.id.tv_sign_up -> {
                navigator.toSignUpFragment()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeState(_viewModel)
    }

    private fun observeState(viewModel: LoginViewModel) {
        viewModel.isSignedIn.observe(viewLifecycleOwner, Observer { isSignedIn ->
            context?.let { context ->
                if (isSignedIn) {
                    startActivity(MapsActivity.newInstance(context))
                    activity?.finish()
                }
            }
        })
    }

    private fun googleSignIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN)
    }
}