package com.example.aniplex.DataLayer.aniplexApi

data class Result(
    val episodeId: String,
    val episodeNumber: Int,
    val id: String,
    val image: String,
    val title: String,
    val url: String
)