package com.example.covid19.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid19.models.CovidData
import com.example.covid19.repository.MainRepository
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    val mainRepository: MainRepository
) : ViewModel() {
        private val getCovidData = mainRepository.getCovidData()

        fun insertCovidData(covidData: CovidData) = viewModelScope.launch {
            mainRepository.insertCovidData(covidData)
        }
}