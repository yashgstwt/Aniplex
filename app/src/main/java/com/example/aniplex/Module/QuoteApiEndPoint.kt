package com.example.aniplex.Module

import com.example.aniplex.DataLayer.QuoteApi.RandomQuote
import retrofit2.http.GET

interface QuoteApiEndPoint {
    @GET("random")
    suspend fun getRandomQuotes():RandomQuote
}