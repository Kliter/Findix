package com.kl.findix.services

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.kl.findix.model.ServiceResult
import com.kl.findix.model.ServiceResult.Failure
import com.kl.findix.model.ServiceResult.Success
import java.io.ByteArrayOutputStream

class ImageServiceImpl : ImageService {

    companion object {
        private const val TAG = "ImageServiceImpl"
        private const val COMPRESS_QUALITY = 100
    }

    override fun getBitmap(uri: Uri, contentResolver: ContentResolver): ServiceResult<Bitmap> {
        return runCatching {
            MediaStore.Images.Media.getBitmap(contentResolver, uri)
        }.fold(
            onSuccess = { Success(it) },
            onFailure = { Failure(it as Exception) }
        )
    }

    override fun getBytesFromBitmap(bitmap: Bitmap): ServiceResult<ByteArray> {
        return runCatching {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY, stream)
            stream.toByteArray()
        }.fold(
            onSuccess = {
                Success(it)
            },
            onFailure = {
                Failure(it as Exception)
            }
        )
    }
}