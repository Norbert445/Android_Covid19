package com.example.covid19.ui


import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.example.covid19.R
import com.example.covid19.utils.Resource
import com.example.covid19.viewModels.MainViewModel
import com.ybs.countrypicker.CountryPicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    lateinit var progressDialog: ProgressDialog
    lateinit var lastCountry: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressDialog = ProgressDialog(this@MainActivity)
        progressDialog.setMessage("Loading...")

        mainViewModel.getCovidData()

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
                    if(it.message == "Check internet connection") {
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
                }

                is Resource.Error -> {
                    progressDialog.dismiss()

                    val alertDialogBuilder = AlertDialog.Builder(this@MainActivity)
                    alertDialogBuilder.setTitle("Error")
                    alertDialogBuilder.setMessage(it.message)
                    alertDialogBuilder.setCancelable(false)
                    if(it.message == "Check internet connection") {
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

        btnZmenitKrajinu.setOnClickListener {
            val countryPicker = CountryPicker.newInstance("Choose country")
            countryPicker.setListener { name, code, dialCode, flagDrawableResID ->
                mainViewModel.getCountryData(name)

                tvCountryName.text = name
                ivCountryFlag.setImageResource(flagDrawableResID)
                lastCountry = name

                countryPicker.dismiss()
            }
            countryPicker.show(supportFragmentManager,"COUNTRY_PICKER")
        }

        btnGlobalStatus.setOnClickListener {
            mainViewModel.getCovidData()

            tvCountryName.text = "Global status"
            ivCountryFlag.setImageResource(R.drawable.globe)
        }
    }
}