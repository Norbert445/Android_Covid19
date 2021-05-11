package com.example.covid19.repository

import com.example.covid19.API.CovidStats
import com.example.covid19.models.CovidData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class MainRepository @Inject constructor(
    private val covidStats: CovidStats
) {
    fun getCovidData() =
        covidStats.getGlobalStatus().enqueue(object: Callback<CovidData> {
            override fun onFailure(call: Call<CovidData>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<CovidData>?, response: Response<CovidData>?) {

            }
        })

    fun getCountryData(country: String) =
        covidStats.getCountryStatus(country).enqueue(object: Callback<CovidData> {
            override fun onFailure(call: Call<CovidData>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<CovidData>?, response: Response<CovidData>?) {

            }
        })
}

