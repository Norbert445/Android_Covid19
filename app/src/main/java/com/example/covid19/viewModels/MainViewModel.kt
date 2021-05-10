package com.example.covid19.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid19.models.CovidData
import com.example.covid19.repository.MainRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel @ViewModelInject constructor(
    val mainRepository: MainRepository
) : ViewModel() {

    fun getCovidData() {
        val response = mainRepository.getCovidData()
        Timber.d("Fetched: ${response}")
    }
}