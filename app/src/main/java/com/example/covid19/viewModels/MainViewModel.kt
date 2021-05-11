package com.example.covid19.viewModels

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.ContactsContract
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid19.models.CovidData
import com.example.covid19.repository.MainRepository
import com.example.covid19.utils.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException

class MainViewModel @ViewModelInject constructor(
    @ApplicationContext var context: Context,
    val mainRepository: MainRepository
) : ViewModel() {

    val covidData: MutableLiveData<Resource<CovidData>> = MutableLiveData()
    val countryData: MutableLiveData<Resource<CovidData>> = MutableLiveData()

    fun getCovidData() {
        covidData.postValue(Resource.Loading())
        try {

            if(isOnline()) {

                /*val res = mainRepository.getCovidData()
                if(res.enqueue()) {
                    covidData.postValue(Resource.Success(res.body()))
                } else {
                    covidData.postValue(Resource.Error("Prepáčte, niečo sa pokazilo"))
                    Timber.e(res.message())
                }*/

            } else {
                covidData.postValue(Resource.Error("Skontrolujte pripojenie k sieti"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> covidData.postValue(Resource.Error("Zlyhalo pripojenie"))
                else -> covidData.postValue(Resource.Error("Prepáčte, niečo sa pokazilo"))
            }
            Timber.e(t)
        }
    }

    fun getCountryData(country: String) {
        countryData.postValue(Resource.Loading())
        try {

            if(isOnline()) {

                /*val res = mainRepository.getCountryData(country)
                if(res.isSuccessful) {
                    countryData.postValue(Resource.Success(res.body()))
                } else {
                    countryData.postValue(Resource.Error("Prepáčte, niečo sa pokazilo"))
                }*/

            } else {
                countryData.postValue(Resource.Error("Skontrolujte pripojenie k internetu"))
            }

        }catch(t: Throwable) {
            when(t) {
                is IOException -> countryData.postValue(Resource.Error("Zlyhalo pripojenie"))
                else -> countryData.postValue(Resource.Error("Prepáčte, niečo sa pokazilo"))
            }
        }
    }

    private fun isOnline(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.let {
                val networkCapabilites = it.getNetworkCapabilities(it.activeNetwork) ?: return false
                return when {
                    networkCapabilites.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    networkCapabilites.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    networkCapabilites.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }
        } else {
            connectivityManager.let {
                it.activeNetworkInfo?.run {
                    return when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ContactsContract.CommonDataKinds.Email.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> true
                    }
                }
            }
            return false
        }

    }
}