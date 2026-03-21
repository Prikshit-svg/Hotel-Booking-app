package com.example.internshalaprojects.network

data class GeoNameResponse(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String?
)

data class OtmHotelResponse(
    val features: List<OtmFeature>
)

data class OtmFeature(
    val id: String,
    val geometry: OtmGeometry,
    val properties: OtmProperties
)

data class OtmGeometry(
    val coordinates: List<Double> // [lon, lat]
)

data class OtmProperties(
    val xid: String,
    val name: String,
    val dist: Double?,   // distance in meters
    val rate: Int?,      // rating 0-3
    val kinds: String?   // category tags
)