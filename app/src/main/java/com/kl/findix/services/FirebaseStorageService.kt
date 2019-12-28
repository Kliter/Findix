package com.kl.findix.services

import com.google.firebase.storage.StorageReference

interface FirebaseStorageService {
    fun uploadProfileIcon(userId: String, byteArray: ByteArray)
    fun getProfileIconRef(userId: String): StorageReference
}