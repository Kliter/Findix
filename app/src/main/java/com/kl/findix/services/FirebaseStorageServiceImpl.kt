package com.kl.findix.services

import com.google.firebase.storage.FirebaseStorage
import com.kl.findix.model.ServiceResult
import com.kl.findix.util.extension.getStorageOrderPhotoPath
import com.kl.findix.util.extension.getStorageProfileIconPath
import com.kl.findix.util.extension.getStorageWorkPhotoPath
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseStorageServiceImpl(
    private val storage: FirebaseStorage
) : FirebaseStorageService {

    companion object {
        private const val TAG = "FirebaseStorageService"
    }

    override suspend fun uploadProfilePhoto(userId: String, byteArray: ByteArray) =
        suspendCoroutine<ServiceResult<Unit>> { continuation ->
            try {
                val profilePhotoReference = storage.reference.child(
                    getStorageProfileIconPath(
                        userId
                    )
                )
                profilePhotoReference.putBytes(byteArray)
                    .addOnSuccessListener {
                        continuation.resume(ServiceResult.Success(Unit))
                    }
                    .addOnFailureListener {
                        continuation.resume(ServiceResult.Failure(it))
                    }
            } catch (e: Exception) {
                continuation.resume(ServiceResult.Failure(e))
            }
        }

    override suspend fun getProfilePhotoRef(userId: String) =
        storage.reference.child(getStorageProfileIconPath(userId))

    override suspend fun uploadOrderPhoto(userId: String, orderId: String, byteArray: ByteArray) =
        suspendCoroutine<ServiceResult<Unit>> { continuation ->
            try {
                val orderPhotoReference = storage.reference.child(
                    getStorageOrderPhotoPath(userId, orderId)
                )
                orderPhotoReference.putBytes(byteArray)
                    .addOnSuccessListener {
                        continuation.resume(ServiceResult.Success(Unit))
                    }
                    .addOnFailureListener {
                        continuation.resume(ServiceResult.Failure(it))
                    }
            } catch (e: Exception) {
                continuation.resume(ServiceResult.Failure(e))
            }
        }

    override suspend fun getOrderPhotoRef(userId: String, orderId: String) =
        storage.reference.child(
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

    override suspend fun getWorkPhotos(userId: String, number: Int) =
        storage.reference.child(
            getStorageWorkPhotoPath(
                userId,
                number
            )
        )
}