package com.kl.findix.services

interface FirebaseStorageService {
    fun uploadProfileIcon(userId: String, byteArray: ByteArray)
}