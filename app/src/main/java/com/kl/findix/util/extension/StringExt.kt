package com.kl.findix.util.extension

import com.kl.findix.util.FIREBASE_IMAGE_STORAGE
import java.text.SimpleDateFormat
import java.util.*

fun getStorageProfileIconPath(userId: String) = "$FIREBASE_IMAGE_STORAGE/$userId/profile_photo"

fun isNotEmptyAndBlank(text: String): Boolean {
    return text.isNotEmpty() && text.isNotBlank()
}

fun getDateTimeText(timeStamp: Date): String {
    val format = SimpleDateFormat("HH:mm, MM/dd, yyyy")
    return format.format(timeStamp)
}

fun getStorageOrderPhotoPath(userId: String, orderId: String) = "$FIREBASE_IMAGE_STORAGE/$userId/$orderId"