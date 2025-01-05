package com.android.Weather.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.Weather.Activity.MainActivity
import com.android.Weather.Model.CityResponseApi
import com.android.Weather.R

import com.android.Weather.databinding.CityViewholderBinding


class CityAdapter:RecyclerView.Adapter<CityAdapter.ViewHolder>() {
    private lateinit var binding: CityViewholderBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding= CityViewholderBinding.inflate(inflater,parent,false)
        return ViewHolder()
    }
    inner class ViewHolder:RecyclerView.ViewHolder(binding.root)
    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = CityViewholderBinding.bind(holder.itemView)
        binding.cityname.text = differ.currentList[position].name

        val sharedPreferences: SharedPreferences = binding.root.context.getSharedPreferences("WeatherPrefs", Context.MODE_PRIVATE)

        binding.root.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.putFloat("Lat", differ.currentList[position].lat!!.toFloat())  // Storing latitude
            editor.putFloat("Lon", differ.currentList[position].lon!!.toFloat())  // Storing longitude
            editor.putString("name", differ.currentList[position].name)        // Storing city name
            editor.apply()

            val intent = Intent(binding.root.context, MainActivity::class.java)
            binding.root.context.startActivity(intent)

            // Add transition animation
            if (binding.root.context is Activity) {
                (binding.root.context as Activity).overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        }
    }


    private val differCallback=object: DiffUtil.ItemCallback<CityResponseApi.CityResponseApiItem>(){
        override fun areItemsTheSame(
            oldItem: CityResponseApi.CityResponseApiItem,
            newItem: CityResponseApi.CityResponseApiItem
        ): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(
            oldItem: CityResponseApi.CityResponseApiItem,
            newItem: CityResponseApi.CityResponseApiItem
        ): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,differCallback)

}