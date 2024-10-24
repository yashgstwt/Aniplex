package com.example.aniplex.RoomDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface FavDAO {

    @Query("SELECT * FROM Favourite")
    fun getFavourites(): Flow<List<Favourite>>

    @Upsert
    suspend fun insertAnime(fav: Favourite)

    @Query("DELETE FROM Favourite WHERE animeId = :id")
    suspend fun removeAnime(id :String )

    @Query("SELECT EXISTS(SELECT 1 FROM Favourite WHERE animeId = :id) ")
    suspend fun isFavourite(id:String):Boolean

}