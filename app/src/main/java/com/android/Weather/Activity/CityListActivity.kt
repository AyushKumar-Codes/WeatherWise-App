package com.android.Weather.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.Weather.Adapter.CityAdapter
import com.android.Weather.Model.CityResponseApi
import com.android.Weather.R
import com.android.Weather.ViewModel.CityViewModel
import com.android.Weather.databinding.ActivityCityListBinding
import retrofit2.Response

class CityListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCityListBinding
    private val cityAdapter by lazy { CityAdapter() }
    private val cityViewModel: CityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflate the binding
        binding = ActivityCityListBinding.inflate(layoutInflater)

        // Set the content view to the
        // root of the binding
        setContentView(binding.root)


        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT

        // Set the initial state of the chip navigator
        binding.chipNavigator.setItemSelected(R.id.explorer, true)
        binding.chipNavigator.setOnItemSelectedListener { menuId ->
            when (menuId) {
                R.id.home -> {
                    startActivity(Intent(this@CityListActivity,MainActivity::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                }
                R.id.profile -> {
                    startActivity(Intent(this@CityListActivity,ProfileActivity::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                }
                R.id.bookmark -> {
                    startActivity(Intent(this@CityListActivity,BookmarkActivity::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                }

            }
        }
        // Set up the search functionality
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(s: Editable?) {
                binding.progressBar2.visibility = View.VISIBLE
                cityViewModel.loadCity(s.toString(), 10).enqueue(object : retrofit2.Callback<CityResponseApi> {
                    override fun onResponse(
                        call: retrofit2.Call<CityResponseApi>,
                        response: Response<CityResponseApi>
                    ) {
                        if (response.isSuccessful) {
                            val data = response.body()
                            data?.let {
                                binding.progressBar2.visibility = View.GONE
                                cityAdapter.differ.submitList(it)
                                binding.cityView.apply {
                                    layoutManager = LinearLayoutManager(this@CityListActivity, LinearLayoutManager.HORIZONTAL, false)
                                    adapter = cityAdapter
                                }
                            }
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<CityResponseApi>, t: Throwable) {
                        Toast.makeText(this@CityListActivity, t.toString(), Toast.LENGTH_SHORT).show()
                    }
                })
            }
        })
    }
}
