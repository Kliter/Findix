package com.kl.findix.util.extension

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView

object ImageViewExt {
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
}
