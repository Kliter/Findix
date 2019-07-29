package com.kl.findix.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.api.GoogleApiClient
import com.kl.findix.R
import com.kl.findix.databinding.ActivityLoginBinding
import com.kl.findix.ui.map.MapActivity
import com.kl.findix.util.REQUEST_CODE_SIGN_IN
import com.kl.findix.viewmodel.LoginViewModel
import com.kl.findix.viewmodel.ViewModelFactory
import dagger.android.AndroidInjection
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val TAG = "LoginActivity"
    }

    @Inject lateinit var mViewModelFactory: ViewModelFactory
    @Inject lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var mViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setupWidgets()
    }

    private fun setupWidgets() {
        setupDataBinding()
        setupViewModel()

        // Tmp implementation.
        mViewModel.signOut()
    }

    private fun setupViewModel() {
        mViewModel.getUserLiveData().observe(this, Observer { user ->
            user?.let {
                startActivity(MapActivity.newInstance(this))
                finish()
            }
        })
    }

    private fun setupDataBinding() {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(LoginViewModel::class.java)
        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        binding.listener = this
        binding.btnGoogleSignIn.setOnClickListener(this)
    }

    private fun googleSignIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_google_sign_in -> {
                Log.d(TAG, "Pressed GoogleSignIn Button.")
                googleSignIn()
            }
            R.id.btn_email_sign_in -> {
                //Todo: Implementation.
                Log.d(TAG, "Pressed EmailSignIn Button.")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            val googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (googleSignInResult.isSuccess) {
                mViewModel.signIn(googleSignInResult.signInAccount)
            }
        }
    }
}
