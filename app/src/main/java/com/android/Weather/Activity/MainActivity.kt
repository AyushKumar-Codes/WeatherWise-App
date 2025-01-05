package com.android.Weather.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.Weather.Adapter.ForecastAdapter
import com.android.Weather.Adapter.HourlyAdapter
import com.android.Weather.Model.CurrentResponseApi
import com.android.Weather.Model.ForecastResponseApi
import com.android.Weather.R
import com.android.Weather.ViewModel.WeatherViewModel
import com.android.Weather.databinding.ActivityMainBinding
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Response
import java.util.Calendar


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private val weatherViewModel:WeatherViewModel by viewModels()
    private val calendar by lazy { Calendar.getInstance() }
    private val hourlyAdapter by lazy { HourlyAdapter() }
    private val forecastAdapter by lazy { ForecastAdapter() }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.chipNavigator.setItemSelected(R.id.home,true);
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT

        binding.apply {




        val sharedPreferences: SharedPreferences = getSharedPreferences("WeatherPrefs", MODE_PRIVATE)
        var lat = sharedPreferences.getFloat("Lat", 51.58f).toDouble()
        var lon = sharedPreferences.getFloat("Lon", -0.12f).toDouble()
            var name = sharedPreferences.getString("name", "London")



//            Bookmarker


            val sharedPreferencesBook: SharedPreferences = getSharedPreferences("Bookmarks", MODE_PRIVATE)

            BookBtn.setOnClickListener {
                val bookmarkKey = "${name}_lat"

                // Check if the bookmark already exists in SharedPreferences
                val isAlreadyBookmarked = sharedPreferencesBook.contains(bookmarkKey)

                if (isAlreadyBookmarked) {
                    // If already bookmarked, remove it and update the button to 'book' image
                    deleteBookmark(name!!) // Delete the bookmark
                    chgBookmark(name)

                } else {
                    // If not bookmarked, save it and update the button to 'selected_book' image
                    saveBookmark(name!!, lat, lon) // Save the bookmark
                    chgBookmark(name)
                }

                // Toggle the bookmark state

            }
            val bookmarkKey = "${name}_lat"
            val isAlreadyBookmarked = sharedPreferencesBook.contains(bookmarkKey)
            if (isAlreadyBookmarked) {
                // If already bookmarked, remove it and update the button to 'book' image
                binding.BookBtn.setImageResource(R.drawable.selected_book) // Set the unselected image
                // Delete the bookmark
            } else {
                // If not bookmarked, save it and update the button to 'selected_book' image
                binding.BookBtn.setImageResource(R.drawable.book) // Set the selected image
                // Save the bookmark
            }




            addCity.setOnClickListener{
                startActivity(Intent(this@MainActivity,CityListActivity::class.java))
            }
            CurrCity.text = name
            detailedLayout.visibility = View.GONE
            progressBar.visibility = View.VISIBLE

            chipNavigator.setOnItemSelectedListener { menuId ->
                when (menuId) {
                    R.id.explorer -> {
                        startActivity(Intent(this@MainActivity,CityListActivity::class.java))
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    }
                    R.id.profile -> {
                        startActivity(Intent(this@MainActivity,ProfileActivity::class.java))
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    }
                    R.id.bookmark -> {
                        startActivity(Intent(this@MainActivity,BookmarkActivity::class.java))
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    }


                }
            }



            weatherViewModel.loadCurrentWeather(lat,lon,"metric").enqueue(object :
                retrofit2.Callback<CurrentResponseApi> {
                override fun onResponse(
                    call: Call<CurrentResponseApi>,
                    response: Response<CurrentResponseApi>
                ) {
                    if(response.isSuccessful){
                        val data=response.body()
                        progressBar.visibility = View.GONE
                        detailedLayout.visibility = View.VISIBLE
                        data?.let {
                            weatherStatus.text= it.weather?.get(0)?.main?:"-"
                            windTxt.text = it.wind?.speed?.toString()+" km/hr"
                            tempTxt.text=it.main?.temp?.let{Math.round(it).toString()}+"°C"
                            humTxt.text=it.main?.humidity?.toString()+"%"
                            rainTxt.text=it.visibility?.let{(it/1000).toString()}+" km"
                            MaxTxt.text=it.main?.tempMax?.let{Math.round(it).toString()}+"°C"
                            MinTxt.text=it.main?.tempMin?.let{Math.round(it).toString()}+"°C"
                            val drawable=if(isNightNow()) R.drawable.night_bg
                            else{
                                setDynamicallyWallpaper(it.weather?.get(0)?.icon?:"-")
                            }
                            bgImg.setImageResource(drawable)
                            val icon = when(it.weather?.get(0)?.icon?:"-"){
                                "0d","0n"->"sunny"
                                "02d","02n"->"cloudy_sunny"
                                "03d","03n"->"cloudy_sunny"
                                "04d","04n" ->"cloudy"
                                "09d","09n"->"rainy"
                                "10d","10n"->"rainy"
                                "11d","11n"->"storm"
                                "13d","13n"->"snowy"
                                "50d","50n"->"windy"
                                else->"sunny"
                            }

                            val drawableResourceId: Int=binding.root.resources.getIdentifier(
                                icon,
                                "drawable",binding.root.context.packageName
                            )
                            Glide.with(binding.root.context)
                                .load(drawableResourceId)
                                .into(binding.weatherPic)
                        }
                    }
                }

                override fun onFailure(call: Call<CurrentResponseApi>, t: Throwable) {
                    Toast.makeText(this@MainActivity,t.toString(),Toast.LENGTH_SHORT).show()
                }

            })


//            Forecast temp
            weatherViewModel.loadForecastWeather(lat, lon, "metric").enqueue(object : retrofit2.Callback<ForecastResponseApi> {
                override fun onResponse(
                    call: Call<ForecastResponseApi>,
                    response: Response<ForecastResponseApi>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        data?.let {
                            // Safely handle the nullable list
                            val weatherList = it.list ?: emptyList()

                            // Use filterTodayData to filter only today's weather data
                            hourlyAdapter.filterTodayData(weatherList)

                            // Set up the RecyclerView
                            hourlyweather.apply {
                                layoutManager = LinearLayoutManager(
                                    this@MainActivity,
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                                adapter = hourlyAdapter
                            }
                            forecastAdapter.filterNotTodayData(weatherList)
                            forecast.apply{
                                layoutManager = LinearLayoutManager(
                                    this@MainActivity,
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                                adapter = forecastAdapter
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<ForecastResponseApi>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_SHORT).show()
                }
            })



        }




    }
    private fun saveBookmark(name: String, lat: Double, lon: Double) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("Bookmarks", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Store lat, lon, and name with unique keys
        editor.putFloat("${name}_lat", lat.toFloat())
        editor.putFloat("${name}_lon", lon.toFloat())
        editor.putString("${name}_name", name)
        editor.apply()

        // Log the saved data
        Log.d("BookmarkDebug", "Saved bookmark: Name = $name, Latitude = $lat, Longitude = $lon")
    }

    fun deleteBookmark(name: String) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("Bookmarks", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Remove keys associated with the bookmark
        editor.remove("${name}_lat")
        editor.remove("${name}_lon")
        editor.remove("${name}_name")
        editor.apply()

        // Log the deleted bookmark
        Log.d("BookmarkDebug", "Deleted bookmark: Name = $name")
    }

    private fun chgBookmark(name: String){
        val sharedPreferencesBook: SharedPreferences = getSharedPreferences("Bookmarks", MODE_PRIVATE)
        val bookmarkKey = "${name}_lat"
        val isAlreadyBookmarked = sharedPreferencesBook.contains(bookmarkKey)
        if (isAlreadyBookmarked) {
            // If already bookmarked, remove it and update the button to 'book' image
            binding.BookBtn.setImageResource(R.drawable.selected_book) // Set the unselected image
            // Delete the bookmark
        } else {
            // If not bookmarked, save it and update the button to 'selected_book' image
            binding.BookBtn.setImageResource(R.drawable.book) // Set the selected image
            // Save the bookmark
        }
    }

    private fun isNightNow():Boolean{
        return calendar.get(Calendar.HOUR_OF_DAY)>=18
    }
    private fun setDynamicallyWallpaper(icon:String):Int{
        return when(icon.dropLast(1)){
            "13"->{
                R.drawable.snow_bg
            }
            "01"->{
                R.drawable.sunny_bg
            }
            "02","03","04"->{
                R.drawable.cloudy_bg
            }
            "09","10","11"->{
                R.drawable.rainy_bg
            }
            "50"->{
                R.drawable.haze_bg
            }
            else ->0
        }
    }









}