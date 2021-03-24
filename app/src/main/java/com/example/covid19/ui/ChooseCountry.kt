package com.example.covid19.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import com.example.covid19.R
import kotlinx.android.synthetic.main.activity_choose_country.*

class ChooseCountry : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_country)

        setSupportActionBar(toolbar as androidx.appcompat.widget.Toolbar)
        supportActionBar?.apply {
            setTitle("Krajiny")
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}