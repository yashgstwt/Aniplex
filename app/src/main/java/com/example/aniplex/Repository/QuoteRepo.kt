package com.example.aniplex.Repository

import com.example.aniplex.DataLayer.QuoteApi.RandomQuote
import com.example.aniplex.Module.QuoteApiEndPoint
import javax.inject.Inject
interface Quote_api_Interface {
    suspend fun getRandomQuote():RandomQuote
}
class QuoteRepo @Inject constructor( var quoteApi :QuoteApiEndPoint ):Quote_api_Interface {
    override suspend fun getRandomQuote(): RandomQuote  {
        return  quoteApi.getRandomQuotes()
    }

}