package com.example.traveldiary

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.traveldiary.data.models.Theme
import com.example.traveldiary.ui.ThemeViewModel
import com.example.traveldiary.ui.TravelDiaryNavGraph
import com.example.traveldiary.ui.TravelDiaryRoute
import com.example.traveldiary.ui.composables.AppBar
import com.example.traveldiary.ui.theme.TravelDiaryTheme
import com.example.traveldiary.utils.position.LocationService
import com.example.traveldiary.utils.position.PermissionStatus
import com.example.traveldiary.utils.position.StartMonitoringResult
import com.example.traveldiary.utils.position.rememberPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import org.koin.androidx.compose.koinViewModel


class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var position = Position(41.9028f, 12.4964f)
    private lateinit var locationService: LocationService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Places.initialize(applicationContext, "AIzaSyB6IrzeS3vCCiPHToNAG5u0tZkIyJx1IbM")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationService = LocationService(this)

        setContent {
            val themeViewModel = koinViewModel<ThemeViewModel>()
            val themeState by themeViewModel.state.collectAsStateWithLifecycle()

            TravelDiaryTheme (
                darkTheme = when (themeState.theme) {
                    Theme.Light -> false
                    Theme.Dark -> true
                    Theme.System -> isSystemInDarkTheme()
                }
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val snackbarHostState = remember { SnackbarHostState() }
                    var showLocationDisabledAlert by remember { mutableStateOf(false) }
                    var showPermissionDeniedAlert by remember { mutableStateOf(false) }
                    var showPermissionPermanentlyDeniedSnackbar by remember { mutableStateOf(false) }

                    val locationPermission = rememberPermission(
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) { status ->
                        when (status) {
                            PermissionStatus.Granted -> {
                                val res = locationService.requestCurrentLocation()
                                showLocationDisabledAlert = res == StartMonitoringResult.GPSDisabled
                            }

                            PermissionStatus.Denied ->
                                showPermissionDeniedAlert = true

                            PermissionStatus.PermanentlyDenied ->
                                showPermissionPermanentlyDeniedSnackbar = true

                            PermissionStatus.Unknown -> {}
                        }
                    }

                    fun requestLocation() {
                        if (locationPermission.status.isGranted) {
                            val res = locationService.requestCurrentLocation()
                            showLocationDisabledAlert = res == StartMonitoringResult.GPSDisabled
                        } else {
                            locationPermission.launchPermissionRequest()

                        }
                    }

                    val navController = rememberNavController()
                    val backStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute by remember {
                        derivedStateOf {
                            TravelDiaryRoute.routes.find {
                                it.route == backStackEntry?.destination?.route
                            } ?: TravelDiaryRoute.LogIn
                        }
                    }
                    val ctx = LocalContext.current

                    Scaffold(
                        topBar = { AppBar(navController, currentRoute) },
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                    ) { contentPadding ->
                        position.setLatitude(locationService.coordinates?.latitude?.toFloat() ?: position.latitude.value!!)
                        position.setLongitude(locationService.coordinates?.longitude?.toFloat() ?: position.longitude.value!!)
                        TravelDiaryNavGraph(
                            navController,
                            modifier =  Modifier.padding(contentPadding),
                            position = position,
                            onPosition = { requestLocation() },
                            themeState = themeState,
                            onThemeSelected = themeViewModel::changeTheme,
                        )
                    }
                    if (showLocationDisabledAlert) {
                        AlertDialog(
                            title = { Text("Location disabled") },
                            text = { Text("Location must be enabled to get your current location in the app.") },
                            confirmButton = {
                                TextButton(onClick = {
                                    locationService.openLocationSettings()
                                    showLocationDisabledAlert = false
                                }) {
                                    Text("Enable")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showLocationDisabledAlert = false }) {
                                    Text("Dismiss")
                                }
                            },
                            onDismissRequest = { showLocationDisabledAlert = false }
                        )
                    }

                    if (showPermissionDeniedAlert) {
                        AlertDialog(
                            title = { Text("Location permission denied") },
                            text = { Text("Location permission is required to get your current location in the app.") },
                            confirmButton = {
                                TextButton(onClick = {
                                    locationPermission.launchPermissionRequest()
                                    showPermissionDeniedAlert = false
                                }) {
                                    Text("Grant")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showPermissionDeniedAlert = false }) {
                                    Text("Dismiss")
                                }
                            },
                            onDismissRequest = { showPermissionDeniedAlert = false }
                        )
                    }

                    if (showPermissionPermanentlyDeniedSnackbar) {
                        LaunchedEffect(snackbarHostState) {
                            val res = snackbarHostState.showSnackbar(
                                "Location permission is required.",
                                "Go to Settings",
                                duration = SnackbarDuration.Long
                            )
                            if (res == SnackbarResult.ActionPerformed) {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", ctx.packageName, null)
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                                if (intent.resolveActivity(ctx.packageManager) != null) {
                                    ctx.startActivity(intent)
                                }
                            }
                            showPermissionPermanentlyDeniedSnackbar = false
                        }
                    }
                }
            }
        }
    }
    override fun onPause() {
        super.onPause()
        locationService.pauseLocationRequest()
    }

    override fun onResume() {
        super.onResume()
        locationService.resumeLocationRequest()
    }
}

class Position (
    latitude : Float,
    longitude : Float
) {
    private val _latitude = MutableLiveData(latitude)
    val latitude : LiveData<Float> = _latitude
    private val _longitude = MutableLiveData(longitude)
    val longitude : LiveData<Float> = _longitude

    fun setLatitude(latitude: Float) {
        _latitude.value = latitude
    }

    fun setLongitude (longitude: Float) {
        _longitude.value = longitude
    }
}