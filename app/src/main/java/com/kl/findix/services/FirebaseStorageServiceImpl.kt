package com.kl.findix.services

import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.kl.findix.util.extension.getStorageOrderPhotoPath
import com.kl.findix.util.extension.getStorageProfileIconPath

class FirebaseStorageServiceImpl(
    private val storage: FirebaseStorage
) : FirebaseStorageService {

    companion object {
        private const val TAG = "FirebaseStorageService"
    }

    override fun uploadProfileIcon(userId: String, byteArray: ByteArray) {
        val profilePhotoReference = storage.reference.child(
            getStorageProfileIconPath(
                userId
            )
        )
        profilePhotoReference.putBytes(byteArray).apply {
            this.addOnSuccessListener {
                Log.d(TAG, "profile photo upload success")
            }.addOnFailureListener {
                Log.d(TAG, "profile photo upload failure")
            }
        }
    }

    override fun getProfileIconRef(userId: String) =
        storage.reference.child(
            getStorageProfileIconPath(
                userId
            )
        )

    override fun uploadOrderPhoto(userId: String, orderId: String, byteArray: ByteArray) {
        val orderPhotoReference = storage.reference.child(
            getStorageOrderPhotoPath(
                userId,
                orderId
            )
        )
        orderPhotoReference.putBytes(byteArray).addOnSuccessListener {
            Log.d(TAG, "Order photo is uploaded successfully.")
        }
    }

    override fun getOrderPhotoRef(userId: String, orderId: String) = storage.reference.child(
        getStorageOrderPhotoPath(
            userId = userId,
            orderId = orderId
        )
    )

    override fun deleteOrderPhoto(userId: String, orderId: String) {
        val orderPhotoReference = storage.reference.child(
            getStorageOrderPhotoPath(
                userId,
                orderId
            )
        )
        orderPhotoReference.delete()
    }
}