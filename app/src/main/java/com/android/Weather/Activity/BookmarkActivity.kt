package com.android.Weather.Activity

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.android.Weather.Adapter.BookmarkerAdapter
import com.android.Weather.Model.CurrentResponseApi
import com.android.Weather.R
import com.android.Weather.databinding.ActivityBookmarkBinding
import retrofit2.Call
import retrofit2.Response
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.Weather.ViewModel.WeatherViewModel


class BookmarkActivity : AppCompatActivity() {
    private val weatherViewModel: WeatherViewModel by viewModels()
    private lateinit var binding: ActivityBookmarkBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT

        binding.chipNavigator.setItemSelected(R.id.bookmark, true)
        binding.chipNavigator.setOnItemSelectedListener { menuId ->
            when (menuId) {
                R.id.explorer -> {
                    startActivity(Intent(this@BookmarkActivity,CityListActivity::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                }
                R.id.profile -> {
                    startActivity(Intent(this@BookmarkActivity,ProfileActivity::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                }
                R.id.home->{
                    startActivity(Intent(this@BookmarkActivity,MainActivity::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                }

            }
        }

        val bookmarks = getAllBookmarks()
        val weatherList = mutableListOf<BookmarkerAdapter.WeatherData>()

        if (bookmarks.isEmpty()) {
            binding.progressBar3.visibility = View.GONE
            binding.emptybookmessage.visibility =View.VISIBLE
        } else {
            // Proceed with the network requests if bookmarks are not empty
            for (bookmark in bookmarks) {
                val (name, lat, lon) = bookmark
                weatherViewModel.loadCurrentWeather(lat, lon, "metric").enqueue(object : retrofit2.Callback<CurrentResponseApi> {
                    override fun onResponse(
                        call: Call<CurrentResponseApi>,
                        response: Response<CurrentResponseApi>
                    ) {
                        if (response.isSuccessful) {
                            val data = response.body()
                            binding.emptybookmessage.visibility =View.GONE
                            binding.progressBar3.visibility = View.GONE
                            data?.let {
                                val weatherStatus = it.weather?.get(0)?.main ?: "-"
                                val windSpeed = it.wind?.speed?.toString() + " km/hr"
                                val temperature = it.main?.temp?.let { Math.round(it).toString() } + "°C"
                                val humidity = it.main?.humidity?.toString() + "%"
                                val MaxTxt = it.main?.tempMax?.let { Math.round(it).toString() } + "°C"
                                val MinTxt = it.main?.tempMin?.let { Math.round(it).toString() } + "°C"
                                val icon = when (it.weather?.get(0)?.icon ?: "-") {
                                    "01d", "01n" -> "sunny"
                                    "02d", "02n" -> "cloudy_sunny"
                                    "03d", "03n" -> "cloudy_sunny"
                                    "04d", "04n" -> "cloudy"
                                    "09d", "09n" -> "rainy"
                                    "10d", "10n" -> "rainy"
                                    "11d", "11n" -> "storm"
                                    "13d", "13n" -> "snowy"
                                    "50d", "50n" -> "windy"
                                    else -> "sunny"
                                }

                                val context = this@BookmarkActivity
                                val iconResId = context.resources.getIdentifier(icon, "drawable", context.packageName)

                                weatherList.add(
                                    BookmarkerAdapter.WeatherData(
                                        name = name,
                                        lat = lat,
                                        lon = lon,
                                        temperature = temperature,
                                        humidity = humidity,
                                        windSpeed = windSpeed,
                                        weatherStatus = weatherStatus,
                                        maxtemp = MaxTxt,
                                        mintemp = MinTxt,
                                        iconResId = iconResId
                                    )
                                )

                                // Notify RecyclerView adapter about the data update
                                binding.bookmarker.adapter?.notifyDataSetChanged()
                            }
                        }
                    }

                    override fun onFailure(call: Call<CurrentResponseApi>, t: Throwable) {
                        Log.e("WeatherFetch", "Failed to fetch weather for $name: ${t.message}")
                    }
                })
            }
        }

// Set adapter to RecyclerView
        binding.bookmarker.layoutManager = LinearLayoutManager(this)
        val adapter = BookmarkerAdapter(weatherList,this)
            binding.bookmarker.adapter = adapter



    }
    private fun getAllBookmarks(): List<Triple<String, Double, Double>> {
        val sharedPreferences: SharedPreferences = getSharedPreferences("Bookmarks", MODE_PRIVATE)
        val allEntries = sharedPreferences.all
        val bookmarks = mutableListOf<Triple<String, Double, Double>>()

        Log.d("BookmarksDebug", "All SharedPreferences entries: $allEntries")

        for ((key, value) in allEntries) {
            if (key.endsWith("_lat")) {
                val baseName = key.substringBefore("_lat")
                val lat = value as Float
                val lon = sharedPreferences.getFloat("${baseName}_lon", 0f).toDouble()
                val name = sharedPreferences.getString("${baseName}_name", null)

                Log.d("BookmarksDebug", "Processing key: $key, baseName: $baseName, lat: $lat, lon: $lon, name: $name")

                if (name != null) {
                    bookmarks.add(Triple(name, lat.toDouble(), lon))
                    Log.d("BookmarksDebug", "Added bookmark: $name, $lat, $lon")
                } else {
                    Log.d("BookmarksDebug", "Skipped due to null name for baseName: $baseName")
                }
            }
        }

        Log.d("BookmarksDebug", "Final bookmarks list: $bookmarks")
        return bookmarks
    }



}