package com.example.aniplex.Repository


import com.example.aniplex.DataLayer.aniplexApi.AnimeInfo
import com.example.aniplex.DataLayer.aniplexApi.RecentEpisodes
import com.example.aniplex.DataLayer.aniplexApi.Search
import com.example.aniplex.DataLayer.aniplexApi.StreamingData
import com.example.aniplex.DataLayer.aniplexApi.TopAiring
import com.example.aniplex.Module.AniplexApiEndPoints
import javax.inject.Inject

interface AniplexRepoInter {

    suspend fun getTopAirings(page : Int ): TopAiring
    suspend fun getRecentEpisodes(page :Int = 1 , type : Int = 1 ): RecentEpisodes
    suspend fun getAnimeInfo(animeID: String): AnimeInfo
    suspend fun getStreamingLink(episodeId: String, serverName: String): StreamingData
    suspend fun getSearchResult(animeName:String , page:Int): Search

}

class AniplexRepo @Inject constructor (private var api : AniplexApiEndPoints) : AniplexRepoInter{
    override suspend fun getTopAirings(page : Int): TopAiring {
        return  api.getTopAirings(page)
    }

    override suspend fun getRecentEpisodes(page :Int  , type : Int  ): RecentEpisodes {
       return api.getRecentEpisodes(page , type )
    }

    override suspend fun getAnimeInfo(animeID: String): AnimeInfo {
       return api.getAnimeInfo(animeID)
    }

    override suspend fun getStreamingLink(episodeId: String, serverName: String): StreamingData {
       return api.getStreamingLink(episodeId, serverName)
    }

    override suspend fun getSearchResult(animeName:String , page:Int): Search {
        return api.Search(animeName, page)
    }
}