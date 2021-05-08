package com.example.covid19.ui

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email.TYPE_MOBILE
import android.util.Log
import com.example.covid19.API.CovidStats
import com.example.covid19.models.CovidData
import com.example.covid19.R
import com.example.covid19.utils.Constants.BASE_URL
import com.google.gson.GsonBuilder
import com.ybs.countrypicker.CountryPicker
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var covidStats: CovidStats

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gson = GsonBuilder().create()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        covidStats = retrofit.create(CovidStats::class.java)

        getGlobalData()

        btnZmenitKrajinu.setOnClickListener {
            val countryPicker = CountryPicker.newInstance("Vyberte si krajinu")
            countryPicker.setListener { name, code, dialCode, flagDrawableResID ->

                if(!isOnline(this@MainActivity)) {
                    countryPicker.dismiss()
                    val alertDialogBuilder = AlertDialog.Builder(this@MainActivity)
                    alertDialogBuilder.setTitle("Error")
                    alertDialogBuilder.setMessage("No internet Connection")
                    alertDialogBuilder.setPositiveButton("Ok") { dialog,which ->
                        dialog.dismiss()
                    }
                    alertDialogBuilder.show()
                    return@setListener
                }


                val progressDialog = ProgressDialog(this@MainActivity)
                progressDialog.setMessage("Načítava sa")
                progressDialog.show()

                covidStats.getCountryStatus(name).enqueue(object: Callback<CovidData> {
                    override fun onFailure(call: Call<CovidData>?, t: Throwable?) {
                        progressDialog.dismiss()
                        Log.e(TAG,"On failure: $t")
                    }

                    override fun onResponse(call: Call<CovidData>?, response: Response<CovidData>?) {
                        val data = response?.body()

                        if(data == null) {
                            val alertDialogBuilder = AlertDialog.Builder(this@MainActivity)
                            alertDialogBuilder.setTitle("Error")
                            alertDialogBuilder.setMessage("$name is not registered")
                            alertDialogBuilder.setPositiveButton("Ok") { dialog,which ->
                                dialog.dismiss()
                            }
                            alertDialogBuilder.show()
                            progressDialog.dismiss()
                            Log.w(TAG,"Didn't receive any message")
                            return
                        }

                        tvCountryName.text = name
                        ivCountryFlag.setImageResource(flagDrawableResID)

                        tvTotalCasesRes.text = data?.cases.toString()
                        tvTodayCasesRes.text = data?.todayCases.toString()
                        tvActiveRes.text = data?.active.toString()
                        tvCriticalRes.text = data?.critical.toString()
                        tvRecoveredRes.text = data?.recovered.toString()
                        tvDeathsRes.text = data?.deaths.toString()

                        progressDialog.dismiss()
                        Log.i(TAG,"On response: $response")
                    }
                })

                countryPicker.dismiss()
            }
            countryPicker.show(supportFragmentManager,"COUNTRY_PICKER")
        }

        btnGlobalStatus.setOnClickListener {
            getGlobalData()
        }
    }

    private fun getGlobalData() {
        if(tvCountryName.text.equals("Global Status")) return

        if(!isOnline(this@MainActivity)) {
            val alertDialogBuilder = AlertDialog.Builder(this@MainActivity)
            alertDialogBuilder.setTitle("Error")
            alertDialogBuilder.setMessage("No internet Connection")
            alertDialogBuilder.setPositiveButton("Ok") { dialog,which ->
                getGlobalData()
            }
            alertDialogBuilder.show()
            return
        }

        val progressDialog = ProgressDialog(this@MainActivity)
        progressDialog.setMessage("Načítava sa")
        progressDialog.show()

        tvCountryName.text = "Global Status"
        ivCountryFlag.setImageResource(R.drawable.globe)
        covidStats.getGlobalStatus().enqueue(object: Callback<CovidData> {
            override fun onFailure(call: Call<CovidData>?, t: Throwable?) {
                progressDialog.dismiss()
                Log.e(TAG,"On failure: $t")
            }

            override fun onResponse(call: Call<CovidData>?, response: Response<CovidData>?) {
                val data = response?.body()
                if(data == null) {
                    Log.w(TAG,"Didn't receive any message")
                }

                tvTotalCasesRes.text = data?.cases.toString()
                tvTodayCasesRes.text = data?.todayCases.toString()
                tvActiveRes.text = data?.active.toString()
                tvCriticalRes.text = data?.critical.toString()
                tvRecoveredRes.text = data?.recovered.toString()
                tvDeathsRes.text = data?.deaths.toString()

                progressDialog.dismiss()

                Log.i(TAG,"On response: $response")
            }
        })
    }

    private fun isOnline(context: Context):Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.let {
                val networkCapabilites = it.getNetworkCapabilities(it.activeNetwork) ?: return false
                return when {
                    networkCapabilites.hasTransport(TRANSPORT_WIFI) -> true
                    networkCapabilites.hasTransport(TRANSPORT_CELLULAR) -> true
                    networkCapabilites.hasTransport(TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }
        } else {
            connectivityManager.let {
                it.activeNetworkInfo?.run {
                    return when (type) {
                        TYPE_WIFI -> true
                        TYPE_MOBILE -> true
                        TYPE_ETHERNET -> true
                        else -> true
                    }
                }
            }
            return false
        }

    }
}