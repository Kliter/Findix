package com.kl.findix.model

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Photo(
    var bitmap: Bitmap? = null,
    var uri: Uri? = null
) : Parcelable