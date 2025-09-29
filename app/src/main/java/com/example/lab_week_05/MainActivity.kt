package com.example.lab_week_05

// Ensure all necessary imports are present
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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


    private val moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .addConverterFactory(MoshiConverterFactory.create(moshi)) // Use configured Moshi
            .build()
    }

    private val catApiService by lazy {
        retrofit.create(CatApiService::class.java)
    }


    private lateinit var apiResponseView: TextView
    private lateinit var imageResultView: ImageView


    private val imageLoader: ImageLoader by lazy {
        GlideLoader(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiResponseView = findViewById(R.id.api_response)
        imageResultView = findViewById(R.id.image_result)

        getCatImageResponse()
    }

    private fun getCatImageResponse() {
        val call = catApiService.searchImages(limit = 1, size = "full", hasBreeds = 1)


        call.enqueue(object : Callback<List<ImageData>> {
            override fun onFailure(call: Call<List<ImageData>>, t: Throwable) {
                Log.e(TAG, "Failed to get response", t)
                apiResponseView.text = "Error: ${t.message}"
            }

            override fun onResponse(call: Call<List<ImageData>>, response: Response<List<ImageData>>) {
                if (response.isSuccessful) {
                    val imageList = response.body()
                    val imageResponse = imageList?.firstOrNull()

                    if (imageResponse == null) {
                        Log.d(TAG, "Response body is null or empty.")
                        apiResponseView.text = "No image data received."
                        return
                    }

                    val currentImageUrl = imageResponse.imageUrl.orEmpty()
                    val breedName = imageResponse.breeds?.firstOrNull()?.name ?: "Unknown Breed"

                    apiResponseView.text = getString(R.string.cat_breed_placeholder, breedName)

                    if (currentImageUrl.isNotBlank()) {
                        imageLoader.loadImage(currentImageUrl, imageResultView)
                    } else {
                        Log.d(TAG, "Missing image URL")
                        imageResultView.setImageResource(android.R.drawable.stat_notify_error)
                    }
                } else {
                    val errorMsg = response.errorBody()?.string().orEmpty()
                    Log.e(TAG, "API call failed: ${response.code()} - $errorMsg")
                    apiResponseView.text = "API Error: ${response.code()}"
                }
            }
        })
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
