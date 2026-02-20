package com.example.internshalaprojects.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Internetitem(
    @SerialName("desc")
    val desc:String,
    @SerialName("image")
    val image: String,
    @SerialName("name")
    val name: String
)
