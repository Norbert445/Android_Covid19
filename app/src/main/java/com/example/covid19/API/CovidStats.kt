package com.example.covid19.API

import com.example.covid19.models.CovidData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CovidStats {
    @GET("all")
   fun getGlobalStatus(): Call<CovidData>
    @GET("countries/{country}")
    fun getCountryStatus(@Path("country") country: String): Call<CovidData>
}