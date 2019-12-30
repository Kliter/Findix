package com.kl.findix.util

fun getStorageProfileIconPath(userId: String) = "$FIREBASE_IMAGE_STORAGE/$userId/profile_photo"

fun isNotEmptyAndBlank(text: String): Boolean {
    return text.isNotEmpty() && text.isNotBlank()
}
