package com.example.covid19.ui


import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.example.covid19.API.CovidStats
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressDialog = ProgressDialog(this@MainActivity)
        progressDialog.setMessage("Načítava sa")

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
                    alertDialogBuilder.setTitle("Chyba")
                    alertDialogBuilder.setMessage(it.message)
                    alertDialogBuilder.setPositiveButton("Ok") { dialog, which ->
                        dialog.dismiss()
                    }
                    alertDialogBuilder.show()
                }
                is Resource.Loading ->
                    progressDialog.show()
            }
        })


        /*val gson = GsonBuilder().create()
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
        }*/
    }

/*private fun getGlobalData() {
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
}*/

}