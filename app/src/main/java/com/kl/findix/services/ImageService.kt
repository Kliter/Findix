package com.kl.findix.services

import android.graphics.Bitmap
import com.kl.findix.model.ServiceResult

interface ImageService {
    fun getBitmap(imageUrl: String): ServiceResult<Bitmap>
    fun getBytesFromBitmap(bitmap: Bitmap): ByteArray
}