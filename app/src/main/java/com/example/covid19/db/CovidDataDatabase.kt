package com.example.covid19.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.covid19.models.CovidData

@Database(
    entities = [CovidData::class],
    version = 1
)
abstract class CovidDataDatabase : RoomDatabase() {
    abstract fun getCovidDataDao(): CovidDataDao
}