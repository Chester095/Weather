package com.geekbrains.weather.view.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.geekbrains.weather.R
import com.geekbrains.weather.databinding.ActivityLocationBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*

class LocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityLocationBinding

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Карта"
        checkPermission()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private val permissionResultLocation =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)
            val coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)
            // описываем сценарии
            when {
                // разрешения выданы
                fineLocationGranted or coarseLocationGranted -> showLocation()
                !ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) -> {
                    // диалог запроса разрешения
                    AlertDialog.Builder(this).setTitle("Дай доступ")
                        .setMessage("очень надо, а то не получится координаты узнать")
                        .setPositiveButton("Дать доступ") { _, _ -> checkPermission() }
                        .setNegativeButton("Не давать") { dialog, _ -> dialog.dismiss() }
                        .create()
                        .show()
                }
                else -> {
                    Toast.makeText(this, "noooo...", Toast.LENGTH_LONG).show()
                    checkPermission()
                }
            }
        }

    @SuppressLint("MissingPermission")
    private fun showLocation() {
        // отвечает за все координаты из всех источников
        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // проверяем какие локации нам доступны
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            val providerGps = locationManager.getProvider(LocationManager.GPS_PROVIDER)

            providerGps?.let {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, // от куда брать данные
                    60_000L, // как часто, раз в час
                    100F, // приблизительная дистанция

                    object : LocationListener {
                        // когда location поменяется то мы его получим
                        override fun onLocationChanged(location: Location) {
                            getAddressByLocation(location)
                        }

                        // не нужная фигня, но без неё не работает (бага разработчиков)
                        override fun onStatusChanged(
                            provider: String?,
                            status: Int,
                            extras: Bundle?
                        ) {}
                    }
                )
            } ?:// если нет получаем последниее координаты, которые нам дали
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let { location ->
                getAddressByLocation(location)
            }
        }
    }


    // адрес по координатам
    private fun getAddressByLocation(location: Location) {
        val geocoder = Geocoder(this)
        Thread {
            try {
                // по координатам получаем адрес
                val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)

                runOnUiThread {
                    AlertDialog.Builder(this)
                        .setTitle("Я тут был " + (Date().time - location.time) / 60 + " минут назад")
                        .setMessage("Адрес -   ${address[0].getAddressLine(0)}")
                        .create()
                        .show()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun checkPermission() {
        permissionResultLocation.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        Log.d("!!! onMapReady","mMap"+mMap)
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}