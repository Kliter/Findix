package com.kl.findix.util

import android.graphics.Bitmap
import android.view.View
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

@BindingAdapter("app:showPhotoIfExist")
fun ImageView.showPhotoIfExist(url: String?) {
    url?.isNotBlank()?.let {
        if (it) {
            this.visibility = View.VISIBLE
        } else {
            this.visibility = View.GONE
        }
    }
}