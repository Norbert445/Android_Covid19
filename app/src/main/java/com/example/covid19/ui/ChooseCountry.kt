package com.example.covid19.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.covid19.Adapters.Adapter
import com.example.covid19.R
import kotlinx.android.synthetic.main.activity_choose_country.*
import java.util.*

class ChooseCountry : AppCompatActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var data: MutableList<String>
    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_country)

        linearLayoutManager = LinearLayoutManager(this)
        rvCountries.layoutManager = linearLayoutManager

        data = (resources.getStringArray(R.array.countries)).toMutableList()
        adapter = Adapter(data)
        rvCountries.adapter = adapter

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)

        val search = menu?.findItem(R.id.search)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "Vyhladajte krajinu"

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                /*if(newText!!.isNotEmpty()) {
                    val search = newText.toLowerCase(Locale.getDefault())
                    data.forEach {
                        if(it.toLowerCase(Locale.getDefault()).contains(search)) {
                            data.add(it)
                        }
                    }
                    rvCountries.adapter?.notifyDataSetChanged()
                }*/
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }
}