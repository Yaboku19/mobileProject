package com.example.traveldiary

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.traveldiary.ui.TravelDiaryNavGraph
import com.example.traveldiary.ui.TravelDiaryRoute
import com.example.traveldiary.ui.composables.AppBar
import com.example.traveldiary.ui.theme.TravelDiaryTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places


class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var position = Position(41.9028f, 12.4964f)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Places.initialize(applicationContext, "AIzaSyB6IrzeS3vCCiPHToNAG5u0tZkIyJx1IbM")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getLastLocation()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        setContent {
            TravelDiaryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val backStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute by remember {
                        derivedStateOf {
                            TravelDiaryRoute.routes.find {
                                it.route == backStackEntry?.destination?.route
                            } ?: TravelDiaryRoute.LogIn
                        }
                    }


                    Scaffold(
                        topBar = { AppBar(navController, currentRoute) }
                    ) { contentPadding ->
                        TravelDiaryNavGraph(
                            navController,
                            modifier =  Modifier.padding(contentPadding),
                            position = position,
                            onPosition = fun() {
                                when (PackageManager.PERMISSION_GRANTED) {
                                    ContextCompat.checkSelfPermission(
                                        this, Manifest.permission.ACCESS_FINE_LOCATION
                                    ) -> {
                                        getLastLocation()

                                    }
                                    else -> {
                                        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    private fun getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    Toast.makeText(this, "Lat: ${location.latitude}, Lng: ${location.longitude}", Toast.LENGTH_LONG).show()
                    position.setLatitude(location.latitude.toFloat())
                    position.setLongitude(location.longitude.toFloat())
                } else {
                    Toast.makeText(this, "No location found", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

class Position (
    latitude : Float,
    longitude : Float
) {
    private val _latitude = MutableLiveData<Float> (latitude)
    val latitude : LiveData<Float> = _latitude
    private val _longitude = MutableLiveData<Float> (longitude)
    val longitude : LiveData<Float> = _longitude

    fun setLatitude(latitude: Float) {
        _latitude.value = latitude
    }

    fun setLongitude (longitude: Float) {
        _longitude.value = longitude
    }
}