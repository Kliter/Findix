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
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.kl.findix.R
import com.kl.findix.databinding.ActivityLoginBinding
import com.kl.findix.ui.map.MapsActivity
import com.kl.findix.ui.signup.SignUpActivity
import com.kl.findix.util.REQUEST_CODE_SIGN_IN
import com.kl.findix.di.ViewModelFactory
import dagger.android.AndroidInjection
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val TAG = "LoginActivity"
    }

    @Inject lateinit var mViewModelFactory: ViewModelFactory
    @Inject lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(LoginViewModel::class.java)

        setupWidgets()
    }

    override fun onStart() {
        super.onStart()
        if (mViewModel.getCurrentSignInUser() != null) {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupWidgets() {
        setupDataBinding()
        setupViewModel()
    }

    private fun setupViewModel() {
        mViewModel.user.observe(this, Observer { user ->
            user?.let {
                startActivity(MapsActivity.newInstance(this))
                finish()
            }
        })
    }

    private fun setupDataBinding() {
        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        binding.listener = this
        binding.btnGoogleSignIn.setOnClickListener(this)
    }

    private fun googleSignIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_google_sign_in -> {
                googleSignIn()
            }
            R.id.btn_email_sign_in -> {
                Log.d(TAG, "Pressed EmailSignIn Button.")
            }
            R.id.tv_sign_up -> {
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            val task = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (task.isSuccess) {
                mViewModel.signIn(task.signInAccount)
            }
        }
    }
}
