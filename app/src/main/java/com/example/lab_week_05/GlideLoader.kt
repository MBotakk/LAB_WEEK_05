package com.example.lab_week_05 // Or your preferred package

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

class GlideLoader(private val context: Context) : ImageLoader {
    override fun loadImage(imageUrl: String, imageView: ImageView) {
        Glide.with(context)
            .load(imageUrl)
            .placeholder(android.R.drawable.picture_frame) // Optional
            .error(android.R.drawable.stat_notify_error)   // Optional
            .into(imageView)
    }
}