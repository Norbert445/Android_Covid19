package com.example.covid19.ui


import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.covid19.R
import com.example.covid19.utils.Constants.SHARED_PREF_COUNTRY
import com.example.covid19.utils.Constants.SHARED_PREF_COUNTRY_IMAGE
import com.example.covid19.utils.Resource
import com.example.covid19.viewModels.MainViewModel
import com.ybs.countrypicker.CountryPicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    lateinit var progressDialog: ProgressDialog
    private var lastCountry: String = ""
    private var lastCountryImage: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressDialog = ProgressDialog(this@MainActivity)
        progressDialog.setMessage("Loading...")

        fetchCovidDataIfNoCountryIsSaved()

        setObservers()

        btnZmenitKrajinu.setOnClickListener {
            val countryPicker = CountryPicker.newInstance("Choose country")
            countryPicker.setListener { name, code, dialCode, flagDrawableResID ->
                mainViewModel.getCountryData(name)

                tvCountryName.text = name
                ivCountryFlag.setImageResource(flagDrawableResID)
                lastCountry = name
                lastCountryImage = flagDrawableResID

                countryPicker.dismiss()
            }
            countryPicker.show(supportFragmentManager, "COUNTRY_PICKER")
        }

        btnGlobalStatus.setOnClickListener {
            mainViewModel.getCovidData()

            tvCountryName.text = "Global status"
            ivCountryFlag.setImageResource(R.drawable.globe)
        }
    }

    private fun fetchCovidDataIfNoCountryIsSaved() {
        val country = retrieveFromSharedPref()
        val image = retrieveFromSharedPrefImage()
        if (country == "") {
            mainViewModel.getCovidData()
        } else {
            mainViewModel.getCountryData(country)
            tvCountryName.text = country
            ivCountryFlag.setImageResource(image)
        }
    }

    private fun setObservers() {
        mainViewModel.covidData.observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    progressDialog.dismiss()

                    tvTotalCasesRes.text = it.data?.cases.toString()
                    tvTodayCasesRes.text = it.data?.todayCases.toString()
                    tvActiveRes.text = it.data?.active.toString()
                    tvCriticalRes.text = it.data?.critical.toString()
                    tvRecoveredRes.text = it.data?.recovered.toString()
                    tvDeathsRes.text = it.data?.deaths.toString()
                }

                is Resource.Error -> {
                    progressDialog.dismiss()

                    val alertDialogBuilder = AlertDialog.Builder(this@MainActivity)
                    alertDialogBuilder.setTitle("Error")
                    alertDialogBuilder.setMessage(it.message)
                    alertDialogBuilder.setCancelable(false)
                    if (it.message == "Check internet connection") {
                        alertDialogBuilder.setPositiveButton("Try again") { dialog, which ->
                            mainViewModel.getCovidData()
                            dialog.dismiss()
                        }
                    } else {
                        alertDialogBuilder.setPositiveButton("Ok") { dialog, which ->
                            dialog.dismiss()
                        }
                    }

                    alertDialogBuilder.show()

                    tvTotalCasesRes.text = "${0}"
                    tvTodayCasesRes.text = "${0}"
                    tvActiveRes.text = "${0}"
                    tvCriticalRes.text = "${0}"
                    tvRecoveredRes.text = "${0}"
                    tvDeathsRes.text = "${0}"
                }
                is Resource.Loading ->
                    progressDialog.show()
            }
        })

        mainViewModel.countryData.observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    progressDialog.dismiss()

                    tvTotalCasesRes.text = it.data?.cases.toString()
                    tvTodayCasesRes.text = it.data?.todayCases.toString()
                    tvActiveRes.text = it.data?.active.toString()
                    tvCriticalRes.text = it.data?.critical.toString()
                    tvRecoveredRes.text = it.data?.recovered.toString()
                    tvDeathsRes.text = it.data?.deaths.toString()

                    if(lastCountry == "") return@Observer
                    
                    saveToSharedPref(lastCountry)
                    saveToSharedPref(lastCountryImage)
                }

                is Resource.Error -> {
                    progressDialog.dismiss()

                    val alertDialogBuilder = AlertDialog.Builder(this@MainActivity)
                    alertDialogBuilder.setTitle("Error")
                    alertDialogBuilder.setMessage(it.message)
                    alertDialogBuilder.setCancelable(false)
                    if (it.message == "Check internet connection") {
                        alertDialogBuilder.setPositiveButton("Try again") { dialog, which ->
                            mainViewModel.getCountryData(lastCountry)
                            dialog.dismiss()
                        }
                    } else {
                        alertDialogBuilder.setPositiveButton("Ok") { dialog, which ->
                            dialog.dismiss()
                        }
                    }

                    alertDialogBuilder.show()

                    tvTotalCasesRes.text = "${0}"
                    tvTodayCasesRes.text = "${0}"
                    tvActiveRes.text = "${0}"
                    tvCriticalRes.text = "${0}"
                    tvRecoveredRes.text = "${0}"
                    tvDeathsRes.text = "${0}"
                }
                is Resource.Loading ->
                    progressDialog.show()
            }
        })
    }

    private fun saveToSharedPref(country: String) {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(SHARED_PREF_COUNTRY, country)
            apply()
        }
    }

    private fun saveToSharedPref(countryImage: Int) {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt(SHARED_PREF_COUNTRY_IMAGE, countryImage)
            apply()
        }
    }

    private fun retrieveFromSharedPref(): String {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return ""
        val country = sharedPref.getString(SHARED_PREF_COUNTRY, "")
        return country!!
    }

    private fun retrieveFromSharedPrefImage(): Int {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return 0
        val image = sharedPref.getInt(SHARED_PREF_COUNTRY_IMAGE, 0)
        return image!!
    }
}