package com.kl.findix.di.module

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.kl.findix.Application
import com.kl.findix.R
import com.kl.findix.services.FirebaseDataBaseService
import com.kl.findix.services.FirebaseDataBaseServiceImpl
import com.kl.findix.services.FirebaseUserService
import com.kl.findix.services.FirebaseUserServiceImpl
import com.kl.findix.services.MapServiceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        ViewModelModule::class
    ]
)
class AppModule {

    @Singleton
    @Provides
    fun provideAppContext(app: Application): Context = app.applicationContext

    @Singleton
    @Provides
    fun provideGoogleSignInClient(context: Context): GoogleSignInClient {
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.web_client_key))
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(context, googleSignInOption)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseFirestore = FirebaseFirestore.getInstance()
}