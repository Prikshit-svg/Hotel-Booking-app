package com.example.internshalaprojects.network

import com.example.internshalaprojects.data.Internetitem
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL="https://trainings.internshala.com"

private val json = Json { ignoreUnknownKeys = true }

private val retrofit=Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface HotelApiService {
    @GET("/uploads/android/hotelbooking/places.json")
   suspend fun getItems() : List<Internetitem>
}
object HotelApi{
    val retrofitservice: HotelApiService by lazy{
        retrofit.create(
            HotelApiService::class.java
        )
    }
}
