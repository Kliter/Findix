package com.kl.findix.services

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import com.kl.findix.model.ServiceResult

interface ImageService {
    fun getBitmap(uri: Uri, contentResolver: ContentResolver): ServiceResult<Bitmap>
    fun getBytesFromBitmap(bitmap: Bitmap): ByteArray
}