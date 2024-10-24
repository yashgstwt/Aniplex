package com.example.aniplex.Repository

import com.example.aniplex.RoomDb.FavDAO
import com.example.aniplex.RoomDb.Favourite
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface roomDbInterface{

    suspend fun getFavouriteList(): Flow<List<Favourite>>

    suspend fun addFavouriteAnime(id:String , name:String ,imgUrl :String ,dubOrSub:String)

    suspend fun deleteFavAnime(id:String)

    suspend fun  isFavourite(id:String):Boolean
}




class RoomDBRepo @Inject constructor (val dao : FavDAO ) :roomDbInterface {
    override suspend fun getFavouriteList(): Flow<List<Favourite>> {
       return dao.getFavourites()
    }

    override suspend fun addFavouriteAnime(id:String , name:String ,imgUrl :String ,dubOrSub:String ) {
        dao.insertAnime(Favourite(id , name , imgUrl , dubOrSub))
    }

    override suspend fun deleteFavAnime(id: String) {
        dao.removeAnime(id)
    }

    override suspend fun isFavourite(id: String): Boolean {
        return dao.isFavourite(id)
    }
}