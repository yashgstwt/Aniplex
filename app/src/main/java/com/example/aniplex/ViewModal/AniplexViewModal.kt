package com.example.aniplex.ViewModal


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aniplex.Repository.AniplexRepo
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject


@HiltViewModel
class AniplexViewModal @Inject constructor( private val repo : AniplexRepo) : ViewModel() {

    var recentReleased : GetAnimeInfo by mutableStateOf(GetAnimeInfo.Loading)
    private set

    var topAirings : GetTopAirings by mutableStateOf(GetTopAirings.Loading)
    private set

    var popular : GetStreamingData by mutableStateOf(GetStreamingData.Loading)
    private set

    var recentEpisodes : GetRecentEpisodes by mutableStateOf(GetRecentEpisodes.Loading)
    private set

    var search : GetSearch by mutableStateOf(GetSearch.Loading)

    var selectedAnimeInfoID: String by mutableStateOf("")


    init {
        viewModelScope.launch {
           getRecentEpisode()

        }


    }

//new api
    fun getRecentEpisode(){
        viewModelScope.launch {
            recentEpisodes = try {
             //   Log.d("API", "************************************${repo.getRecentEpisodes().toString()}")
                GetRecentEpisodes.Success(repo.getRecentEpisodes(1,2))
            }catch (e:Exception){
                GetRecentEpisodes.Error(e)


            }catch (e: HttpException){
                GetRecentEpisodes.Error(e)
            }
            // Log.d("Api" , "....................${recentEpisodes}")

        }
    }

//old api
    fun getAnimeInfo(){
        viewModelScope.launch {
            recentReleased = try {
                GetAnimeInfo.Success(repo.getAnimeInfo(""))

            }catch (e:Exception){
                GetAnimeInfo.Error(e)
            }catch (e: HttpException){
                GetAnimeInfo.Error(e)
            }
            Log.d("Api" , "${recentReleased.toString()}")
        }
    }


}