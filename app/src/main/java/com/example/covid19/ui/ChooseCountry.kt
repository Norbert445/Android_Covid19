package com.example.covid19.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity.apply
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.covid19.Adapters.Adapter
import com.example.covid19.R
import kotlinx.android.synthetic.main.activity_choose_country.*
import java.util.*

class ChooseCountry : AppCompatActivity(), Adapter.OnItemClickListener {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var data: MutableList<String>
    var filteredList: MutableList<String> = arrayListOf()
    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_country)

        linearLayoutManager = LinearLayoutManager(this)
        rvCountries.layoutManager = linearLayoutManager

        data = (resources.getStringArray(R.array.countries)).toMutableList()
        filteredList = (resources.getStringArray(R.array.countries)).toMutableList()

        adapter = Adapter(this)
        adapter.setItems(data)
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
                if(newText!= null) {
                    val query = newText.toLowerCase(Locale.getDefault())
                    filteredList.clear()
                    data.forEach{
                        if(it.toLowerCase(Locale.getDefault()).contains(query)) {
                            filteredList.add(it)
                        }
                    }
                    adapter.setItems(filteredList)
                    adapter.notifyDataSetChanged()
                }
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onItemClick(position: Int) {
        val countryName = filteredList[position]

        val intent = Intent().apply {
            putExtra("countryName",countryName)
        }
        setResult(Activity.RESULT_OK,intent)
        finish()
    }
}