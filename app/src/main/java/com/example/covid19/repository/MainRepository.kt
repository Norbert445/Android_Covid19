package com.example.covid19.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.ContactsContract
import com.example.covid19.API.CovidStats
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val covidStats: CovidStats
) {
    fun getCovidData() =
        covidStats.getGlobalStatus()

    fun getCountryData(country: String) =
        covidStats.getCountryStatus(country)

    private fun isOnline(context: Context):Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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