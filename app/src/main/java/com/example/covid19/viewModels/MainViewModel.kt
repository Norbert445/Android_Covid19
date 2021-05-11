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
    val covidData = mainRepository.covidData
    val countryData = mainRepository.countryData

    fun getCovidData() {
        if (isOnline()) {
            mainRepository.getCovidData()
        } else {
            covidData.postValue(Resource.Error("Skontrolujte pripojenie k internetu"))
        }
    }

    fun getCountryData(country: String) {
        if (isOnline()) {
            mainRepository.getCountryData(country)
        } else {
            countryData.postValue(Resource.Error("Skontrolujte pripojenie k internetu"))
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