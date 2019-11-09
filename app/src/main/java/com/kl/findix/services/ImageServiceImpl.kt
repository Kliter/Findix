package com.kl.findix.services

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.kl.findix.model.ServiceResult
import com.kl.findix.model.ServiceResult.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

class ImageServiceImpl: ImageService {

    companion object {
        private const val TAG = "ImageServiceImpl"
        private const val COMPRESS_QUALITY = 100
    }

    override fun getBitmap(imageUrl: String): ServiceResult<Bitmap> {
        val imageFile = File(imageUrl)
        var fileInputStream: FileInputStream? = null
        return try {
            fileInputStream = FileInputStream(imageFile)
            Success(BitmapFactory.decodeStream(fileInputStream))
        } catch (e: FileNotFoundException) {
            Failure("getBitmap: FileNotFoundException: ${e.message}")
        } finally {
            try {
                fileInputStream?.close()
            } catch (e: IOException) {
                Log.e(TAG, "getBitmap: IOException: ${e.message}")
            }
        }
    }

    override fun getBytesFromBitmap(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY, stream)
        return stream.toByteArray()
    }
}