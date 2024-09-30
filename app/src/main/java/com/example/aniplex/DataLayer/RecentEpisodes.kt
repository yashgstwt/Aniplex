package com.example.aniplex.DataLayer

data class RecentEpisodes(
    val currentPage: Int,
    val hasNextPage: Boolean,
    val results: List<Result>
)