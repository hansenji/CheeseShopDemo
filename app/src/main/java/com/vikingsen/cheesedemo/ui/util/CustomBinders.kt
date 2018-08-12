package com.vikingsen.cheesedemo.ui.util

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vikingsen.cheesedemo.BuildConfig
import com.vikingsen.cheesedemo.R

object CustomBinders {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun bindImage(imageView: ImageView, oldImageUrl: String?, newImageUrl: String?) {
        if (oldImageUrl == newImageUrl) {
            return
        }
        Glide.with(imageView)
            .load(BuildConfig.IMAGE_BASE_URL + newImageUrl)
                .apply(
                    RequestOptions()
                        .centerCrop()
                        .error(R.drawable.ic_broken_image_white_48dp)
                )
                .into(imageView)
    }
}