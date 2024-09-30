package com.example.aniplex.ViewModal


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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


    init {
        viewModelScope.launch {
            try {
                val response = repo.getRecentEpisodes(1,2)
                val gson = GsonBuilder().setPrettyPrinting().create()
                val json = gson.toJson(response)
                Log.d("API Response", json)
            } catch (e: Exception) {
                Log.d("API Response", e.toString())
            }

        }


        //var res = getRecentEpisode()

    }

//new api
    fun getRecentEpisode(){
        viewModelScope.launch {
            recentEpisodes = try {
                Log.d("API", "************************************${repo.getRecentEpisodes().toString()}")
                GetRecentEpisodes.Success(repo.getRecentEpisodes())
            }catch (e:Exception){
                GetRecentEpisodes.Error(e)
            }catch (e: HttpException){
                GetRecentEpisodes.Error(e)
            }
            Log.d("Api" , "....................${recentEpisodes.toString()}")
        }
    }

//old api
    fun getRecentReleased(){
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