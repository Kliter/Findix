package com.kl.findix.services

import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.kl.findix.util.getProfileStorageUrl

class FirebaseStorageServiceImpl(
    private val storage: FirebaseStorage
): FirebaseStorageService {

    companion object {
        private const val TAG = "FirebaseStorageService"
    }

    override fun uploadProfileIcon(userId: String, byteArray: ByteArray) {
        val profilePhotoReference = storage.reference.child(getProfileStorageUrl(userId))
        profilePhotoReference.putBytes(byteArray).apply {
            this.addOnSuccessListener {
                Log.d(TAG, "profile photo upload success")
            }.addOnFailureListener {
                Log.d(TAG, "profile photo upload failure")
            }
        }
    }

    override fun getProfileImageRef(userId: String) {

    }
}