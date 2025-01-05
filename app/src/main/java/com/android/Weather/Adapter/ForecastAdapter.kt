package com.android.Weather.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.Weather.Model.ForecastResponseApi
import com.android.Weather.databinding.ViewholderForecastBinding
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

class ForecastAdapter : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {
    private lateinit var binding: ViewholderForecastBinding

    // Filter out today's data (fetch data for all other dates)
    fun filterNotTodayData(dataList: List<ForecastResponseApi.data>) {
        val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) // Get today's date
        val notTodayData = dataList.filter { it.dtTxt?.startsWith(todayDate) != true } // Exclude today's date
        differ.submitList(notTodayData) // Submit filtered list to differ
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ViewholderForecastBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)  // Pass the binding to the ViewHolder
    }

    inner class ViewHolder(private val binding: ViewholderForecastBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ForecastResponseApi.data) {
            // Parse date
            val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(item.dtTxt.toString())
            val calendar = Calendar.getInstance()
            calendar.time = date

            val dayOfWeekName = when (calendar.get(Calendar.DAY_OF_WEEK)) {
                1 -> "Sun"
                2 -> "Mon"
                3 -> "Tue"
                4 -> "Wed"
                5 -> "Thu"
                6 -> "Fri"
                7 -> "Sat"
                else -> "-"
            }
            binding.DayTxt.text = dayOfWeekName

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
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item) // Bind the item to the ViewHolder
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
