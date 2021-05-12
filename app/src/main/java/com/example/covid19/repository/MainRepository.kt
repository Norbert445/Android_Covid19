package com.example.covid19.repository

import androidx.lifecycle.MutableLiveData
import com.example.covid19.API.CovidStats
import com.example.covid19.models.CovidData
import com.example.covid19.utils.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject


class MainRepository @Inject constructor(
    private val covidStats: CovidStats
) {
    val covidData: MutableLiveData<Resource<CovidData>> = MutableLiveData()
    val countryData: MutableLiveData<Resource<CovidData>> = MutableLiveData()
    val countryName: MutableLiveData<String> = MutableLiveData()

    fun getCovidData() {
        covidData.postValue(Resource.Loading())
        try {
            covidStats.getGlobalStatus().enqueue(object : Callback<CovidData> {
                override fun onFailure(call: Call<CovidData>?, t: Throwable?) {
                    covidData.postValue(Resource.Error("Sorry, something went wrong"))
                }

                override fun onResponse(call: Call<CovidData>?, response: Response<CovidData>) {
                    if (response.isSuccessful) {
                        covidData.postValue(Resource.Success(response.body()))

                    } else {
                        covidData.postValue(Resource.Error("Sorry, something went wrong"))
                    }
                }
            })
        } catch (t: Throwable) {
            when (t) {
                is IOException -> covidData.postValue(Resource.Error("Network failure"))
                else -> covidData.postValue(Resource.Error("Sorry, something went wrong"))
            }
        }
    }

    fun getCountryData(country: String) {
        countryData.postValue(Resource.Loading())
        try {
            covidStats.getCountryStatus(country).enqueue(object : Callback<CovidData> {
                override fun onFailure(call: Call<CovidData>?, t: Throwable?) {
                    countryData.postValue(Resource.Error("Sorry, something went wrong"))
                }

                override fun onResponse(call: Call<CovidData>?, response: Response<CovidData>) {
                    if (response.isSuccessful) {
                        countryData.postValue(Resource.Success(response.body()))
                        countryName.postValue(country)
                    } else {
                        countryData.postValue(Resource.Error("Country is not registered"))
                    }
                }
            })
        } catch (t: Throwable) {
            when (t) {
                is IOException -> countryData.postValue(Resource.Error("Network failure"))
                else -> countryData.postValue(Resource.Error("Sorry, something went wrong"))
            }
        }
    }
}

