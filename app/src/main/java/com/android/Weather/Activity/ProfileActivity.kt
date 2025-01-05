package com.android.Weather.Activity

import android.content.Context
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.Weather.R
import com.android.Weather.databinding.ActivityCityListBinding
import com.android.Weather.databinding.ActivityProfileBinding
import com.android.Weather.databinding.DialogEditNameBinding
import java.io.File

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    private val sharedPreferences by lazy {
        getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    }

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val imagePath = saveImageToInternalStorage(it)

                    saveImagePath(imagePath!!) // Save the file path
                    binding.profilephoto.setImageURI(it)

            }
        }


    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openGallery()
            } else {
                Toast.makeText(this, "Storage permission is required to select an image.", Toast.LENGTH_SHORT).show()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        binding = ActivityProfileBinding.inflate(layoutInflater)

        // Set the content view to the root of the binding
        setContentView(binding.root)


        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT


        binding.nametext.text = loadName()
        loadImagePath()?.let { path ->
            binding.profilephoto.setImageURI(Uri.parse(path))
        }


        // Change Name Button Listener
        binding.changeName.setOnClickListener {
            showNameChangeDialog()
        }

        // Change Image Button Listener
        binding.changePhoto.setOnClickListener {
            handleImageSelection()
        }



        // Set the initial state of the chip navigator
        binding.chipNavigator.setItemSelected(R.id.profile, true)
        binding.chipNavigator.setOnItemSelectedListener { menuId ->
            when (menuId) {
                R.id.home -> {
                    startActivity(Intent(this@ProfileActivity,MainActivity::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                }
                R.id.explorer->{
                    startActivity(Intent(this@ProfileActivity,CityListActivity::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                }
                R.id.bookmark->{
                    startActivity(Intent(this@ProfileActivity,BookmarkActivity::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                }

            }
        }

        val sharedPreferences: SharedPreferences = getSharedPreferences("WeatherPrefs", MODE_PRIVATE)
        var lat = sharedPreferences.getFloat("Lat", 51.58f).toDouble()
        var lon = sharedPreferences.getFloat("Lon", -0.12f).toDouble()
        var name = sharedPreferences.getString("name", "London")
        binding.loc.text=name
        binding.lat.text = String.format("%.3f", lat)
        binding.lon.text = String.format("%.3f", lon)



    }

    private fun showNameChangeDialog() {
        val dialogBinding = DialogEditNameBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogBinding.root)

        dialogBinding.editName.setText(binding.nametext.text) // Set current name in dialog

        builder.setPositiveButton("Save") { dialog, _ ->
            val newName = dialogBinding.editName.text.toString()
            if (newName.isNotBlank()) {
                binding.nametext.text = newName
                saveName(newName) // Save the new name to SharedPreferences
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }
    private fun handleImageSelection() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES)
            == PackageManager.PERMISSION_GRANTED
        ) {
            openGallery() // Permission already granted
        } else {
            // Request permission
            requestPermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
        }
    }

    private fun saveImageToInternalStorage(uri: Uri): String? {
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val fileName = "profile_photo.png"
            val file = File(filesDir, fileName)
            val outputStream = file.outputStream()
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    private fun openGallery() {
        imagePickerLauncher.launch("image/*")
    }

    private fun saveName(name: String) {
        sharedPreferences.edit().putString("user_name", name).apply()
    }

    private fun loadName(): String {
        return sharedPreferences.getString("user_name", "Ayush Sisodiya") ?: "Ayush Sisodiya"
    }

    private fun saveImagePath(path: String) {
        sharedPreferences.edit().putString("image_path", path).apply()
    }

    private fun loadImagePath(): String? {
        return sharedPreferences.getString("image_path", null)
    }




}