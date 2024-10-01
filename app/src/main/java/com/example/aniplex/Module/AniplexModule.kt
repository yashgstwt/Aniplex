package com.example.aniplex.Module




import com.example.aniplex.Repository.AniplexRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AniplexModule{

    @Provides
    @Singleton
    fun provideApi (): ApiEndPoints {
        return Retrofit.Builder()
            .baseUrl("http://192.168.216.222:3000/anime/gogoanime/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiEndPoints::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(api : ApiEndPoints): AniplexRepo {
        return AniplexRepo(api)
    }
}

