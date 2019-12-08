package com.kl.findix.util

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.BindingAdapter
import com.kl.findix.R

@BindingAdapter("app:bitmapSrc")
fun ImageView.setBitmapSrc(bitmap: Bitmap?) {
    if (bitmap == null) {
        this.setImageBitmap(
            ResourcesCompat.getDrawable(
                resources,
                R.mipmap.ic_launcher,
                null
            )?.toBitmap()
        )
    } else {
        this.setImageBitmap(bitmap)
    }
}

