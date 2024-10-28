package com.example.aniplex.DataLayer.aniplexApi

data class RecentEpisodes(
    val currentPage: Int,
    val hasNextPage: Boolean,
    val results: List<Result>
)