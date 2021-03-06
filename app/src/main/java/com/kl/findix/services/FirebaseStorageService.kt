package com.kl.findix.services

import com.google.firebase.storage.StorageReference
import com.kl.findix.model.ServiceResult

interface FirebaseStorageService {
    suspend fun uploadProfilePhoto(userId: String, byteArray: ByteArray): ServiceResult<Unit>
    suspend fun getProfilePhotoRef(userId: String): StorageReference
    suspend fun uploadOrderPhoto(userId: String, orderId: String, byteArray: ByteArray): ServiceResult<Unit>
    suspend fun getOrderPhotoRef(userId: String, orderId: String): StorageReference
    suspend fun uploadWorkPhoto(userId: String, byteArray: ByteArray, number: Int): ServiceResult<Unit>
    suspend fun getWorkPhotoRef(userId: String, number: Int): StorageReference
    suspend fun deleteOrderPhoto(userId: String, orderId: String)
    suspend fun deleteUser(userId: String): ServiceResult<Unit>
}