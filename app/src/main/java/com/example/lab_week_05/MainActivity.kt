package com.example.lab_week_05

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.lab_week_05.api.CatApiServicePart1
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class MainActivity : AppCompatActivity() {
    companion object { private const val TAG = "MainActivity" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiResponse = findViewById<TextView>(R.id.api_response)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val service = retrofit.create(CatApiServicePart1::class.java)
        val call: Call<String> = service.searchImages(1, "full")
        call.enqueue(object : Callback<String> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    apiResponse.text = response.body().orEmpty()
                } else {
                    apiResponse.text = "HTTP ${response.code()}"
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(TAG, "request failed", t)
                apiResponse.text = "Error: " + t.message
            }
        })
    }
}
