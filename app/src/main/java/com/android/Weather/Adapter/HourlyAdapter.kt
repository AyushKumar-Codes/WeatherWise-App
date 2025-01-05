package com.android.Weather.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.Weather.Model.ForecastResponseApi
import com.android.Weather.databinding.ViewholderHourlyBinding
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

class HourlyAdapter : RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {
    private lateinit var binding: ViewholderHourlyBinding

    // Filter today's data
    fun filterTodayData(dataList: List<ForecastResponseApi.data>) {
        val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) // Get today's date
        val todayData = dataList.filter { it.dtTxt?.startsWith(todayDate) == true } // Filter by today's date
        differ.submitList(todayData) // Submit filtered list to differ
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ViewholderHourlyBinding.inflate(inflater, parent, false)
        return ViewHolder()
    }

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = ViewholderHourlyBinding.bind(holder.itemView)
        val item = differ.currentList[position]

        // Parse date
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(item.dtTxt.toString())
        val calendar = Calendar.getInstance()
        calendar.time = date



        // Hour and AM/PM
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val amPM = if (hour < 12) " am" else " pm"
        val hour12 = if (calendar.get(Calendar.HOUR) == 0) 12 else calendar.get(Calendar.HOUR)
        binding.hourTxt.text = "$hour12$amPM"

        // Temperature
        binding.tempTxt.text = item.main?.temp?.let { Math.round(it) }.toString() + "° C"

        // Icon
        val icon = when (item.weather?.get(0)?.icon.toString()) {
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

        // Load drawable
        val drawableResourceId: Int = binding.root.resources.getIdentifier(
            icon,
            "drawable", binding.root.context.packageName
        )
        Glide.with(binding.root.context)
            .load(drawableResourceId)
            .into(binding.pic)
    }

    private val differCallback = object : DiffUtil.ItemCallback<ForecastResponseApi.data>() {
        override fun areItemsTheSame(
            oldItem: ForecastResponseApi.data,
            newItem: ForecastResponseApi.data
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ForecastResponseApi.data,
            newItem: ForecastResponseApi.data
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}
