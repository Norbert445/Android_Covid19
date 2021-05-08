package com.example.covid19.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "covidDataTable"
)
data class CovidData (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val cases: Int,
    val todayCases: Int,
    val active: Int,
    val critical: Int,
    val recovered: Int,
    val deaths: Int
)


