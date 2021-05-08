package com.example.covid19.repository

import com.example.covid19.db.CovidDataDao
import com.example.covid19.models.CovidData
import javax.inject.Inject

class MainRepository @Inject constructor(
    val covidDataDao: CovidDataDao
) {
    suspend fun insertCovidData(covidData: CovidData) = covidDataDao.insert(covidData)

    fun getCovidData() = covidDataDao.getCovidData()

}