package com.example.aniplex.DataLayer.aniplexApi

data class ResultX(
    val genres: List<String>,
    val id: String,
    val image: String,
    val title: String,
    val url: String
)