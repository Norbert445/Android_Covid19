package com.example.covid19.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.covid19.models.CovidData

@Dao
interface CovidDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(covidData: CovidData)

    @Query("SELECT * FROM covidDataTable")
    fun getCovidData(): LiveData<CovidData>
}