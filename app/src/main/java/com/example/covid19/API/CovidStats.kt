package com.example.covid19.API

import com.example.covid19.API.model.CovidData
import retrofit2.Call
import retrofit2.http.GET

interface CovidStats {
    @GET("all")
   fun getAllStates(): Call<CovidData>
}