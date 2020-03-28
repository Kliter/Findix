package com.kl.findix.util.extension

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.Spinner
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.BindingAdapter
import com.kl.findix.R

@BindingAdapter("app:bitmapSrc")
fun ImageView.setBitmapSrc(bitmap: Bitmap?) {
    this.setImageBitmap(bitmap)
}

@BindingAdapter("app:showPhotoIfExist")
fun ImageView.showPhotoIfExist(url: String?) {
    if (url == null) {
        this.visibility = View.GONE
    } else {
        url.isNotEmpty().let {
            if (it) {
                this.visibility = View.VISIBLE
            } else {
                this.visibility = View.GONE
            }
        }
    }
}

@BindingAdapter("android:selectedItemPosition")
fun Spinner.selectedItemPosition(position: Int) {
    this.setSelection(position)
}