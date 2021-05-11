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

    fun getCovidData() {
        covidData.postValue(Resource.Loading())
        try {
            covidStats.getGlobalStatus().enqueue(object : Callback<CovidData> {
                override fun onFailure(call: Call<CovidData>?, t: Throwable?) {
                    covidData.postValue(Resource.Error("Prepáčte, nastala chyba"))
                }

                override fun onResponse(call: Call<CovidData>?, response: Response<CovidData>) {
                    if (response.isSuccessful) {
                        covidData.postValue(Resource.Success(response.body()))

                    } else {
                        covidData.postValue(Resource.Error("Prepáčte, nastala chyba"))
                    }
                }
            })
        } catch (t: Throwable) {
            when (t) {
                is IOException -> covidData.postValue(Resource.Error("Zlyhalo pripojenie"))
                else -> covidData.postValue(Resource.Error("Prepáčte, nastala chyba"))
            }
        }
    }

    fun getCountryData(country: String) {
        countryData.postValue(Resource.Loading())
        try {
            covidStats.getCountryStatus(country).enqueue(object : Callback<CovidData> {
                override fun onFailure(call: Call<CovidData>?, t: Throwable?) {
                    countryData.postValue(Resource.Error("Prepáčte, nastala chyba"))
                }

                override fun onResponse(call: Call<CovidData>?, response: Response<CovidData>) {
                    if (response.isSuccessful) {
                        countryData.postValue(Resource.Success(response.body()))

                    } else {
                        countryData.postValue(Resource.Error("Prepáčte, nastala chyba"))
                    }
                }
            })
        } catch (t: Throwable) {
            when (t) {
                is IOException -> countryData.postValue(Resource.Error("Zlyhalo pripojenie"))
                else -> countryData.postValue(Resource.Error("Prepáčte, nastala chyba"))
            }
        }
    }
}

