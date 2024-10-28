package com.example.aniplex.ViewModal

import com.example.aniplex.DataLayer.QuoteApi.RandomQuote
import com.example.aniplex.DataLayer.aniplexApi.AnimeInfo
import com.example.aniplex.DataLayer.aniplexApi.RecentEpisodes
import com.example.aniplex.DataLayer.aniplexApi.Search
import com.example.aniplex.DataLayer.aniplexApi.StreamingData

import com.example.aniplex.DataLayer.aniplexApi.TopAiring


sealed interface GetTopAirings {
    data class Success(val airings: TopAiring) : GetTopAirings
    data class Error(val message: String) : GetTopAirings
    data object Loading : GetTopAirings
}

sealed interface GetStreamingData {
    data class Success(val streamingData: StreamingData):GetStreamingData
    data class Error(val message: String):GetStreamingData
    data object Loading : GetStreamingData
}

sealed interface GetAnimeInfo {
    data class Success(val animeInfo: AnimeInfo):GetAnimeInfo
    data class Error(val message: Exception):GetAnimeInfo
    data object Loading : GetAnimeInfo
}

sealed interface GetRecentEpisodes {
    data class Success (var data : RecentEpisodes): GetRecentEpisodes
    data class Error(var error : Exception ):GetRecentEpisodes
    data object Loading : GetRecentEpisodes
}

sealed interface GetSearch {
    data class Success(val search: Search):GetSearch
    data class ERROR(val message: String):GetSearch
    data object Loading : GetSearch

}
sealed interface GetQuote{
    data class Success(val quote:RandomQuote):GetQuote
    data class Error(val message: String):GetQuote
    data object Loading : GetQuote

}