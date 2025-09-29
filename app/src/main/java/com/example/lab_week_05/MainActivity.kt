package com.example.lab_week_05

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.lab_week_05.api.CatApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import com.example.lab_week_05.model.ImageData // Tambahkan import ini
import retrofit2.converter.moshi.MoshiConverterFactory // Ganti import scalar dengan moshi

class MainActivity : AppCompatActivity() {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            // Ganti converter ke Moshi [cite: 304]
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    // ... (catApiService dan apiResponseView tetap sama)
    private val catApiService by lazy {
        retrofit.create(CatApiService::class.java)
    }
    private val apiResponseView: TextView by lazy {
        findViewById(R.id.api_response)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getCatImageResponse()
    }

    // Perbarui fungsi ini
    private fun getCatImageResponse() {
        val call = catApiService.searchImages(1, "full")

        // Sesuaikan tipe data Callback menjadi List<ImageData>
        call.enqueue(object : Callback<List<ImageData>> {
            override fun onFailure(call: Call<List<ImageData>>, t: Throwable) {
                Log.e(MAIN_ACTIVITY, "Failed to get response", t)
            }

            override fun onResponse(call: Call<List<ImageData>>, response: Response<List<ImageData>>) {
                if (response.isSuccessful) {
                    val image = response.body() // Dapatkan list gambar
                    // Ambil URL dari gambar pertama, jika tidak ada, gunakan "No URL" [cite: 321]
                    val firstImage = image?.firstOrNull()?.imageUrl ?: "No URL"
                    // Tampilkan URL menggunakan format dari strings.xml [cite: 321]
                    apiResponseView.text = getString(R.string.image_placeholder, firstImage)
                } else {
                    Log.e(MAIN_ACTIVITY, "Failed to get response\n" +
                            response.errorBody()?.string().orEmpty())
                }
            }
        })
    }

    // ... (companion object tetap sama)
    companion object {
        const val MAIN_ACTIVITY = "MAIN_ACTIVITY"
    }
}
