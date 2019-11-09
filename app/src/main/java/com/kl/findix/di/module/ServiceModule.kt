package com.kl.findix.di.module

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.kl.findix.services.FileService
import com.kl.findix.services.FileServiceImpl
import com.kl.findix.services.FirebaseDataBaseService
import com.kl.findix.services.FirebaseDataBaseServiceImpl
import com.kl.findix.services.FirebaseStorageService
import com.kl.findix.services.FirebaseStorageServiceImpl
import com.kl.findix.services.FirebaseUserService
import com.kl.findix.services.FirebaseUserServiceImpl
import com.kl.findix.services.ImageService
import com.kl.findix.services.ImageServiceImpl
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
    fun provideFirebaseUserService(context: Context, firebaseAuth: FirebaseAuth, database: FirebaseFirestore): FirebaseUserService {
        return FirebaseUserServiceImpl(context, firebaseAuth, database)
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

    @Singleton
    @Provides
    fun provideFirebaseStorageService(storage: FirebaseStorage): FirebaseStorageService = FirebaseStorageServiceImpl(storage)

    @Singleton
    @Provides
    fun provideFileService(): FileService = FileServiceImpl()

    @Singleton
    @Provides
    fun provideImageService(): ImageService = ImageServiceImpl()
}