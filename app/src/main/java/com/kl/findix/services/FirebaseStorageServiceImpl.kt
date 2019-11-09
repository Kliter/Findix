package com.kl.findix.services

import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.kl.findix.util.FIREBASE_IMAGE_STORAGE

class FirebaseStorageServiceImpl(
    private val storage: FirebaseStorage
): FirebaseStorageService {

    companion object {
        private const val TAG = "FirebaseStorageService"
    }

    override fun uploadProfileIcon(userId: String, byteArray: ByteArray) {
        val profilePhotoReference =
            storage.reference.child("$FIREBASE_IMAGE_STORAGE/$userId/profile_photo")
        val uploadTask: UploadTask = profilePhotoReference.putBytes(byteArray)
        uploadTask.addOnSuccessListener {
            Log.d(TAG, "profile photo upload success")
        }.addOnFailureListener {
            Log.d(TAG, "profile photo upload failure")
        }
    }
}