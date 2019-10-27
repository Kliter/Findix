package com.kl.findix.di.module

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
class ServiceModule {

    @Singleton
    @Provides
    fun provideFirebaseUserService(context: Context, firebaseAuth: FirebaseAuth): FirebaseUserService {
        return FirebaseUserServiceImpl(context, firebaseAuth)
    }

    @Singleton
    @Provides
    fun provideMapService(context: Context): MapServiceImpl {
        return MapServiceImpl(context)
    }

    @Singleton
    @Provides
    fun provideFirebaseDataBaseService(firebaseFirestore: FirebaseFirestore): FirebaseDataBaseService =
        FirebaseDataBaseServiceImpl(firebaseFirestore)
}