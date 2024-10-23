package com.example.aniplex.RoomDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favourite(
    @PrimaryKey
    val animeId :String = "",
    val name: String,
    val imageUrl: String,
    val dubOrSub: String,
)
