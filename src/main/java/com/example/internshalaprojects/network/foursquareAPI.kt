package com.example.internshalaprojects.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenTripMapService {

    // Step 1 — Convert city name to coordinates
    @GET("0.1/en/places/geoname")
    suspend fun getCityCoordinates(
        @Query("name") cityName: String,
        @Query("apikey") apiKey: String
    ): GeoNameResponse

    // Step 2 — Search hotels near coordinates
    @GET("0.1/en/places/radius")
    suspend fun searchNearbyHotels(
        @Query("radius") radius: Int = 10000,   // 10km radius
        @Query("lon") lon: Double,
        @Query("lat") lat: Double,
        @Query("kinds") kinds: String = "accomodations", // hotels
        @Query("limit") limit: Int = 15,
        @Query("format") format: String = "geojson",
        @Query("apikey") apiKey: String
    ): OtmHotelResponse
}

object OpenTripMapApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.opentripmap.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: OpenTripMapService =
        retrofit.create(OpenTripMapService::class.java)
}