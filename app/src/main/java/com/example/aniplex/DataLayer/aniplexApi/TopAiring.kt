package com.example.aniplex.DataLayer.aniplexApi

data class TopAiring(
    val currentPage: Int,
    val hasNextPage: Boolean,
    val results: List<ResultX>
)