package com.example.aniplex.DataLayer.aniplexApi

data class Source(
    val isM3U8: Boolean,
    val quality: String,
    var url: String
)