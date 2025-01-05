package com.android.Weather.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.Weather.Activity.MainActivity
import com.android.Weather.R
import com.android.Weather.databinding.ViewholderBookmarkerBinding
import com.bumptech.glide.Glide

class BookmarkerAdapter(
    private val weatherList: List<WeatherData>,
    private val context: Context
) : RecyclerView.Adapter<BookmarkerAdapter.WeatherViewHolder>() {

    data class WeatherData(
        val name: String,
        val lat: Double,
        val lon: Double,
        val temperature: String,
        val humidity: String,
        val windSpeed: String,
        val weatherStatus: String,
        val maxtemp: String,
        val mintemp: String,
        val iconResId: Int
    )

    inner class WeatherViewHolder(private val binding: ViewholderBookmarkerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: WeatherData) {
            binding.name.text = data.name
            binding.curtemp.text = data.temperature
            binding.hum.text = data.humidity
            binding.wind.text = data.windSpeed
            binding.Status.text = data.weatherStatus
            binding.max.text = data.maxtemp
            binding.min.text = data.mintemp

            // Use Glide to load the image
            Glide.with(context)
                .load(data.iconResId)
                .into(binding.pic)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = ViewholderBookmarkerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val data = weatherList[position]
        holder.bind(data)

        // Setting up the click listener on the entire item view
        holder.itemView.setOnClickListener {
            // Store data in SharedPreferences
            val sharedPreferences: SharedPreferences = holder.itemView.context.getSharedPreferences(
                "WeatherPrefs", Context.MODE_PRIVATE
            )
            val editor = sharedPreferences.edit()

            // Using safe calls for lat and lon
            data.lat?.let {
                editor.putFloat("Lat", it.toFloat()) // Storing latitude
            }
            data.lon?.let {
                editor.putFloat("Lon", it.toFloat()) // Storing longitude
            }
            editor.putString("name", data.name) // Storing city name
            editor.apply()

            // Start MainActivity
            val intent = Intent(holder.itemView.context, MainActivity::class.java)
            holder.itemView.context.startActivity(intent)

            // Add the transition animation
            if (holder.itemView.context is Activity) {
                (holder.itemView.context as Activity).overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        }
    }


    override fun getItemCount(): Int = weatherList.size
}
