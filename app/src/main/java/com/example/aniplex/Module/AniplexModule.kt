package com.example.aniplex.Module




import android.app.Application
import androidx.room.Room
import com.example.aniplex.Repository.AniplexRepo
import com.example.aniplex.Repository.RoomDBRepo
import com.example.aniplex.RoomDb.AniplexDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
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
           //.baseUrl("http://192.168.216.222:3000/anime/gogoanime/") // local host url
            .baseUrl("https://ainplexapi.vercel.app/anime/gogoanime/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiEndPoints::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(api : ApiEndPoints): AniplexRepo {
        return AniplexRepo(api)
    }

    @Singleton
    @Provides
    fun provideRoomDb(app:Application) : AniplexDataBase {
        return  Room.databaseBuilder(app , AniplexDataBase::class.java,"AniplexDataBase").build()
    }

    @Singleton
    @Provides
    fun provideRoomDbRepo(db : AniplexDataBase) : RoomDBRepo {
        return RoomDBRepo(db.dao())
    }

}