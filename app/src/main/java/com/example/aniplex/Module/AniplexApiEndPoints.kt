package com.example.aniplex.Module


import com.example.aniplex.DataLayer.aniplexApi.AnimeInfo
import com.example.aniplex.DataLayer.aniplexApi.RecentEpisodes
import com.example.aniplex.DataLayer.aniplexApi.Search
import com.example.aniplex.DataLayer.aniplexApi.StreamingData
import com.example.aniplex.DataLayer.aniplexApi.TopAiring
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface AniplexApiEndPoints {

    @GET("{animeName}")
    suspend fun Search(
        @Path("animeName") animeName : String,
        @Query("page") page : Int = 1
    ): Search

    @GET("recent-episodes")
    suspend fun getRecentEpisodes(
        @Query("page") page: Int = 1,
        @Query("type") type : Int = 2
    ): RecentEpisodes

    @GET("top-airing")
    suspend fun getTopAirings(
        @Query("page") page:Int = 1
    ): TopAiring

    @GET("info/{id}")
    suspend fun getAnimeInfo(
        @Path("id") animeID : String
    ): AnimeInfo

    @GET("watch/{episodeId}")
    suspend fun getStreamingLink(
        @Path("episodeId") episodeId : String,
        @Query("serverName") serverName : String
    ): StreamingData
}