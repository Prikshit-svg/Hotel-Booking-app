package com.example.internshalaprojects.data

import androidx.annotation.DrawableRes
import com.example.internshalaprojects.R

data class Hotel(@DrawableRes val image : Int, val name: String, val rating: String, val priceRange: Int,val link:String)


fun ListOfHotels(): List<Hotel>{
    return listOf(Hotel(R.drawable.hotelfoxoso,"Foxoso Hotel","4.6",10000,"https://www.foxosohotels.com/"),
        Hotel(R.drawable.hotelsamovar,"Hotel Samovar","4.5",12000,"https://hotelsamovar.com/"),
        Hotel(R.drawable.grand_mercure_agra_facade,"Grand Mercure Agra","4.7",15000,"https://gmccagra.com/"),
        Hotel(R.drawable.hotelkaranvilla,"Hotel Karanvilla","4.6",10000,"https://www.hotelkaranvilas.com/"),
        Hotel(R.drawable.hotelparadise,"Hotel Paradise","4.5",12000,"https://paradiseinn.in/contacts.html"),
        Hotel(R.drawable.oberoihotels,"Oberoi Hotels","4.7",15000,"https://www.oberoihotels.com/"),
        Hotel(R.drawable.tridenthotel,"Trident hotel","4.6",10000,"https://www.tridenthotel.com/"),
        )

}