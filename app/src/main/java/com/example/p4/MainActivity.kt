package com.example.p4

//import com.android.volley.Request
//import com.android.volley.Response
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.p4.ui.main.MainFragment
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity(), OneCom, MainFragment.MenuSwitch {
    private var name = "USA"
    private val date = "2020-04-01"
    public val key = "762783cf73mshc3a986cc49384bcp1db7cbjsncee48df72e6f"
    public var oneUrl = "https://covid-19-data.p.rapidapi.com/report/country/name?name=${name}&date=${date}&format=json"
    public val host = "covid-19-data.p.rapidapi.com"

    public var globalCountry = ""
    var globalProvince = ""
    var globalConfirmed = ""
    var globalRecovered = ""
    var globalDeaths = ""
    var globalActiveCases = ""

    var apiSuccess = false


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.icon1 -> {
                item.isChecked = !item.isChecked
                SwitchView("One")
                return true
            }
            R.id.icon2 -> {
                item.isChecked = !item.isChecked
                SwitchView("All")
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun SwitchView(viewLbl: String) {
        if(viewLbl == "One"){
            var transaction : FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, OneFragment()).commit()
        }
        else if (viewLbl == "All"){
            var transaction : FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, AllFragment()).commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }
        if (Build.VERSION.SDK_INT > 16) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

    }

    fun ChangeGlobalCountry(country: String) {
        name = country
        oneUrl = "https://covid-19-data.p.rapidapi.com/report/country/name?name=${name}&date=${date}&format=json"
    }

    fun ChangeGlobal(country: String, province: String, confirmed: String,
                     recovered: String, deaths: String, active: String) {
        if(country != "")
            globalCountry = country
        if(province != "")
            globalProvince = province
        if(confirmed != "")
            globalConfirmed = confirmed
        if(recovered != "")
            globalRecovered = recovered
        if(deaths != "")
            globalDeaths = deaths
        if(active != "")
            globalActiveCases = active
    }

    override fun GetOneResponse(): String {
        var httpResp : String = "API call with okHttp"
        val client = OkHttpClient()
        val request = Request.Builder()
                .addHeader("x-rapidapi-key", key)
                .addHeader("x-rapidapi-host", host)
                .url(oneUrl)
                .get()
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body
                if (response.isSuccessful) {
                    ChangeApiStatus(true)
                    val respAry : JSONArray = JSONArray(body!!.string())
                    val respObj = respAry.getJSONObject(0)
                    val outer_country = respObj.getString("country")
                    //enter inner array
                    val provinceAry = respObj.getJSONArray("provinces")
                    val provObj = provinceAry.getJSONObject(0) //GETS ALABAMA
                    val provName = provObj.getString("province")
                    val provConfirmed = provObj.getString("confirmed")
                    val provRecovered = provObj.getString("recovered")
                    val provDeaths = provObj.getString("deaths")
                    val provActive = provObj.getString("active")

                    println("provinceAry: " + provinceAry.toString())
                    println("outer_country: " + outer_country)
                    println("provName: " + provName)
                    println("provConfirmed: " + provConfirmed)
                    println("provRecovered: " + provRecovered)
                    println("provDeaths: " + provDeaths)
                    println("provActive: " + provActive)

                    ChangeGlobal(outer_country, provName, provConfirmed, provRecovered, provDeaths, provActive)
                } else {
                    val errorBody = body.toString()
                    println("error:" + errorBody)
                    ChangeApiStatus(false)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed HTTP execution ")
                println("Fail: " + e.message.toString())
                ChangeApiStatus(false)
            }
        })
        if(apiSuccess == true)
            httpResp = "Country: ${globalCountry} \n\nProvince: ${globalProvince} \n\nConfirmed: ${globalConfirmed} \n\nRecovered: ${globalRecovered} \n\nDeaths: ${globalDeaths} \n\nActive: ${globalActiveCases}"
        else if (apiSuccess == false)
            httpResp = "Failed to call API - Cause may be a connection or input error"
        return httpResp
    }

    fun ChangeApiStatus(status: Boolean){
        apiSuccess = status
    }

}