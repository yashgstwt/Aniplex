package com.example.aniplex.ViewModal


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aniplex.DataLayer.Episode
import com.example.aniplex.DataLayer.Source
import com.example.aniplex.Repository.AniplexRepo
import com.example.aniplex.Repository.RoomDBRepo
import com.example.aniplex.RoomDb.Favourite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject


@HiltViewModel
class AniplexViewModal @Inject constructor( private val repo : AniplexRepo ,  val db : RoomDBRepo) : ViewModel() {

    var AnimeInfo : GetAnimeInfo by mutableStateOf(GetAnimeInfo.Loading)


    var topAirings : GetTopAirings by mutableStateOf(GetTopAirings.Loading)
    private set

    var StreamingLink : GetStreamingData by mutableStateOf(GetStreamingData.Loading)
    private set

    var recentEpisodes : GetRecentEpisodes by mutableStateOf(GetRecentEpisodes.Loading)
    private set


    var search : GetSearch by mutableStateOf(GetSearch.Loading)

    var AnimeEpisodesIDs : List<Episode> by mutableStateOf(emptyList<Episode>())

    var playQuality:List<Source> = listOf( Source(false,"Loading..",""))

    var playbackServer : String by mutableStateOf("gogocdn")

    var topAiringsPage:Int by mutableStateOf(1)
    var recentReleasedPage:Int by mutableStateOf(1)

    init {
        viewModelScope.launch {
            getRecentEpisode()
            delay(5000)
            Log.d("Stream" , recentEpisodes.toString())
            getTopAirings(topAiringsPage)
        }
    }

    fun getTopAirings(page:Int = 1){
        viewModelScope.launch {
            topAirings= try {
                GetTopAirings.Success(repo.getTopAirings(page))
            }catch(e:Exception){
                GetTopAirings.Error(e.toString())
            }
        }
    }


    fun getStreamingLink(animeId: String, server: String) {
        viewModelScope.launch {
            StreamingLink = try {
                GetStreamingData.Success(repo.getStreamingLink(animeId, server))
            }catch (e:Exception){
                GetStreamingData.Error(e.toString())
            }
        }
    }


    fun getRecentEpisode(page:Int = 1 , DubSub:Int = 2){
        viewModelScope.launch {
            recentEpisodes = try {
                GetRecentEpisodes.Success(repo.getRecentEpisodes(page,DubSub))
            }catch (e:Exception){
                GetRecentEpisodes.Error(e)


            }catch (e: HttpException){
                GetRecentEpisodes.Error(e)
            }
        }
    }


    fun getAnimeInfo(AnimeId : String ){
        CoroutineScope(Dispatchers.IO).launch {
            AnimeInfo = try {
                GetAnimeInfo.Success(repo.getAnimeInfo(AnimeId ?: ""))

            }catch (e:Exception){
                GetAnimeInfo.Error(e)
            }catch (e: HttpException){
                GetAnimeInfo.Error(e)
            }
        }
    }

    fun insertFav(id:String , name:String ,imgUrl :String ,dubOrSub:String ){
        CoroutineScope(Dispatchers.IO).launch {
            db.addFavouriteAnime(id,name,imgUrl,dubOrSub)
        }
    }
    fun DeleteFav(id:String  ){
        CoroutineScope(Dispatchers.IO).launch {
            db.deleteFavAnime(id)
        }
    }
    suspend fun isFavourite(id: String): Boolean = withContext(Dispatchers.IO) {
        db.isFavourite(id)
    }
}



//2024-10-06 00:20:57.673 30717-30717 DetailScreen            com.example.aniplex                  D   AnimeInfo(description=Based on a picture book written about the 2004 earthquake in Chuuetsu, Niigata prefecture.
//
//                                                                                                    Mari's three puppies were just born, when the earthquake in Yamakoshimura started. Now their home is in ruins, and their owner, an old man is trapped under the debris. Mari somehow manages to break her chain, and encourages the old man, until help arrives. In the end, someone saves the old man, but he can't take in the dogs, so they remain in the now abandoned village. Can Mari and the puppies live on like this?, episodes=[Episode(id=yamakoshi-mura-no-mari-to-sanbiki-no-koinu-dub-episode-1, number=1, url=https://anitaku.pe//yamakoshi-mura-no-mari-to-sanbiki-no-koinu-dub-episode-1)], genres=[Drama, Kids], id=yamakoshi-mura-no-mari-to-sanbiki-no-koinu-dub, image=https://gogocdn.net/cover/yamakoshi-mura-no-mari-to-sanbiki-no-koinu-dub.png, otherName=Yamakoshi-mura no Mari to Sanbiki no Koinu, Yamakoshi Mura no Mari to San-biki no Koinu, 山古志村のマリと三匹の子犬, releaseDate=2006, status=Completed, subOrDub=dub, title=Yamakoshi Mura no Mari to Sanbiki no Koinu (Dub), totalEpisodes=1, type=OVA, url=https://anitaku.pe/category/yamakoshi-mura-no-mari-to-sanbiki-no-koinu-dub)
//2024-10-06 00:20:57.673 30717-30717 DetailScreen            com.example.aniplex                  D   https://anitaku.pe/category/yamakoshi-mura-no-mari-to-sanbiki-no-koinu-dub