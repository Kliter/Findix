package com.kl.findix.di.module

import android.app.Application
import android.content.Context
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.kl.findix.R
import com.kl.findix.firestore.FirebaseUserService
import com.kl.findix.firestore.UserService
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
    fun provideGoogleApiClient(context: Context): GoogleApiClient {
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.web_client_key))
            .requestEmail()
            .build()

        return GoogleApiClient.Builder(context).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOption).build()
    }

    @Singleton
    @Provides
    fun provideUserService(): UserService = FirebaseUserService()

}