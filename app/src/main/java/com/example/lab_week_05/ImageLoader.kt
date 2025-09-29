package com.example.lab_week_05 // Or your preferred package

import android.widget.ImageView

interface ImageLoader {
    fun loadImage(imageUrl: String, imageView: ImageView)
}