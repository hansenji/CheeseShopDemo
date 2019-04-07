package com.vikingsen.cheesedemo.ui.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vikingsen.cheesedemo.BuildConfig
import com.vikingsen.cheesedemo.R
import org.threeten.bp.LocalDateTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

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

    @JvmStatic
    @BindingAdapter("localDate")
    fun bindLocalDate(textView: TextView, localDateTime: LocalDateTime?) {
        textView.text = localDateTime?.format(FORMATTER)
    }

    @JvmStatic
    @BindingAdapter("date")
    fun bindDate(textView: TextView, dateTime: OffsetDateTime?) {
        textView.text = dateTime?.format(FORMATTER)
    }

    private val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)

}