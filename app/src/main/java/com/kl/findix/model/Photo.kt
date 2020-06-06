package com.kl.findix.model

import android.graphics.Bitmap
import android.net.Uri

data class Photo(
    var bitmap: Bitmap? = null,
    var uri: Uri? = null
)