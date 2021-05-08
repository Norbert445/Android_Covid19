package com.example.covid19.di

import android.content.Context
import androidx.room.Room
import com.example.covid19.API.CovidStats
import com.example.covid19.db.CovidDataDatabase
import com.example.covid19.utils.Constants.BASE_URL
import com.example.covid19.utils.Constants.COVID_DATA_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCovidDataDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        CovidDataDatabase::class.java,
        COVID_DATA_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideCovidDataDao(db: CovidDataDatabase) = db.getCovidDataDao()

    @Provides
    fun provideBaseUrl() = BASE_URL

    @Singleton
    @Provides
    fun provideRetrofit(BASE_URL: String) = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit) = retrofit.create(CovidStats::class.java)
}