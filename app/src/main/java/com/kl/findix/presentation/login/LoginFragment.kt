package com.kl.findix.presentation.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.kl.findix.R
import com.kl.findix.databinding.FragmentLoginBinding
import com.kl.findix.di.ViewModelFactory
import com.kl.findix.navigation.LoginNavigator
import com.kl.findix.util.REQUEST_CODE_SIGN_IN
import com.kl.findix.util.extension.nonNullObserve
import com.kl.findix.util.extension.showToast
import com.kl.findix.util.extension.viewModelProvider
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class LoginFragment : Fragment() {

    companion object {
        private const val TAG = "LoginFragment"
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var mGoogleSignInClient: GoogleSignInClient
    @Inject
    lateinit var navController: NavController

    private lateinit var _viewModel: LoginViewModel
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewModel = viewModelProvider(mViewModelFactory)
        binding = DataBindingUtil.inflate<FragmentLoginBinding>(
            inflater,
            R.layout.fragment_login,
            container,
            false
        ).apply {
            lifecycleOwner = this@LoginFragment
            viewModel = _viewModel
            onClickGoogleSign = View.OnClickListener {
                googleSignIn()
            }
            onClickEmailSignIn = View.OnClickListener {
                _viewModel.signInWithEmail()
            }
            onClickSignUp = View.OnClickListener {
                LoginFragmentDirections.toSignUp()
            }
        }

        _viewModel.isAlreadySignedIn()

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeState(_viewModel)
        activity?.bottom_navigation_view?.visibility = View.VISIBLE
    }

    private fun observeState(viewModel: LoginViewModel) {
        viewModel.signInResult.nonNullObserve(viewLifecycleOwner) { result ->
            context?.let { context ->
                if (result) {
                    showToast(
                        context,
                        getString(R.string.succeed_sign_in)
                    )
                    navController.navigate(
                        LoginFragmentDirections.toMaps()
                    )
                } else {
                    showToast(
                        context,
                        getString(R.string.failed_sign_in)
                    )
                }
            }
        }
        viewModel.isAlreadySignedIn.nonNullObserve(viewLifecycleOwner) {
            if (it) {
                navController.navigate(
                    LoginFragmentDirections.toMaps()
                )
            }
        }
    }

    private fun googleSignIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            val task = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (task.isSuccess) {
                _viewModel.signInWithGoogle(task.signInAccount)
            }
        }
    }
}