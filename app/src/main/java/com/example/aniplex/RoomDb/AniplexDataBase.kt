package com.example.aniplex.RoomDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Favourite::class], version = 1)
abstract class AniplexDataBase : RoomDatabase() {

    abstract fun dao(): FavDAO

}