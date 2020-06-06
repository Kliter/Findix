package com.kl.findix.util.extension

import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.Spinner
import androidx.databinding.BindingAdapter

@BindingAdapter("app:bitmapSrc")
fun ImageView.setBitmapSrc(bitmap: Bitmap?) {
    this.setImageBitmap(bitmap)
}

@BindingAdapter("android:selectedItemPosition")
fun Spinner.selectedItemPosition(position: Int) {
    this.setSelection(position)
}