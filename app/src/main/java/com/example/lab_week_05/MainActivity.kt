package com.example.lab_week_05

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.lab_week_05.api.CatApiService
import com.example.lab_week_05.model.ImageData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.thecatapi.com/v1/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val catApiService: CatApiService by lazy {
        retrofit.create(CatApiService::class.java)
    }

    private lateinit var apiResponseView: TextView
    private lateinit var imageResultView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiResponseView = findViewById(R.id.api_response)
        imageResultView = findViewById(R.id.image_result)

        getCatImageResponse()
    }

    private fun getCatImageResponse() {
        val call = catApiService.searchImages(limit = 1, size = "full", hasBreeds = true)

        call.enqueue(object : Callback<List<ImageData>> {
            override fun onFailure(call: Call<List<ImageData>>, t: Throwable) {
                Log.e(TAG, "Request failed to TheCatAPI", t)
                apiResponseView.text = "Error fetching image: ${t.message}"
                imageResultView.setImageResource(android.R.drawable.stat_notify_error)
            }

            override fun onResponse(call: Call<List<ImageData>>, response: Response<List<ImageData>>) {
                if (response.isSuccessful) {
                    val images = response.body()
                    val firstImageModel = images?.firstOrNull()

                    if (firstImageModel != null && firstImageModel.url != null && firstImageModel.url.isNotBlank()) {
                        val imageUrl = firstImageModel.url
                        apiResponseView.text = getString(R.string.image_url_placeholder, imageUrl)

                        Glide.with(this@MainActivity)
                            .load(imageUrl)
                            .placeholder(android.R.drawable.picture_frame)
                            .error(android.R.drawable.stat_notify_error)
                            .into(imageResultView)

                        Log.d(TAG, "Successfully loaded image URL: $imageUrl")
                    } else {
                        val message = "No image URL found or image list is empty from API."
                        apiResponseView.text = message
                        Log.d(TAG, message)
                        imageResultView.setImageDrawable(null)
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown API error"
                    val errorCode = response.code()
                    val errorMessage = "API Error (Code: $errorCode): $errorBody"
                    Log.e(TAG, errorMessage)
                    apiResponseView.text = "Error: $errorCode (Details in Logcat)"
                    imageResultView.setImageResource(android.R.drawable.stat_notify_error)
                }
            }
        })
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
