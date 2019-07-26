package com.kl.findix.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kl.findix.R
import com.kl.findix.databinding.ActivityLoginBinding
import com.kl.findix.util.REQUEST_CODE_SIGN_IN
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val TAG = "LoginActivity"
    }

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        binding.listener = this
        btn_google_sign_in.setOnClickListener(this)

        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("983690829081-n0lfljm32e8q33bmthuo97i1s9l91rdt.apps.googleusercontent.com")
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser != null) {
            Log.d(TAG, "There is a existing signed-in user.")
        }

        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            Log.d(TAG, "There is a existing signed-in user!")
            firebaseAuthWithGoogle(account)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_google_sign_in -> {
                Log.d(TAG, "Pressed GoogleSignIn Button.")
                signIn()
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
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>?) {
        try {
            val account: GoogleSignInAccount? = completedTask?.getResult(ApiException::class.java)
            if (account != null) {
                Log.d(TAG, "singInResult:succeed account name=${account.displayName}")
                firebaseAuthWithGoogle(account)
            }
        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=${e.statusCode}")
            //Todo: Implementation ui udpate.
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle: ${account.id}")

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        mAuth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "signInWithCredential: success")
                val user = mAuth.currentUser
            }
            else {
                Log.w(TAG, "signInWithCredential:failure", task.exception)
                Snackbar.make(login_container, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                // Todo: Implementation ui update.
            }
        }
    }
}
