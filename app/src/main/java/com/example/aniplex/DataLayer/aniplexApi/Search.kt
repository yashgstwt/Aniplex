package com.example.aniplex.DataLayer.aniplexApi

data class Search(
    val currentPage: Int,
    val hasNextPage: Boolean,
    val results: List<ResultXX>
)