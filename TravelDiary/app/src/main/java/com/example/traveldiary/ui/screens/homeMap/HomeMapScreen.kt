package com.example.traveldiary.ui.screens.homeMap

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.traveldiary.Position
import com.example.traveldiary.data.database.User
import com.example.traveldiary.ui.MarkersState
import com.example.traveldiary.ui.TravelDiaryRoute
import com.example.traveldiary.ui.composables.DropMenu
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeMapScreen(
    user: User,
    navController: NavHostController,
    state : MarkersState,
    latitude : Float,
    longitude : Float,
    onPosition : () -> Unit
) {
    /*val ctx = LocalContext.current

    fun shareDetails() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, place.username)
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share travel")
        if (shareIntent.resolveActivity(ctx.packageManager) != null) {
            ctx.startActivity(shareIntent)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = ::shareDetails
            ) {
                Icon(Icons.Outlined.Share, "Share Travel")
            }
        },
    ) { contentPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(contentPadding).padding(12.dp).fillMaxSize()
        ) {
            Image(
                Icons.Outlined.Image,
                "Travel picture",
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary),
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .size(128.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(36.dp)
            )
            Text(
                place.username,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                place.username,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.size(8.dp))
            Text(
                place.password,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }*/
    var center by remember { mutableStateOf(LatLng(latitude.toDouble(), longitude.toDouble())) }  // Coordinate iniziali di Roma
    var placeLocations by remember { mutableStateOf(listOf<LatLng>()) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(center, 10f, 0f, 0f)
    }
    val context = LocalContext.current
    var showButton by remember { mutableStateOf(false) }
    var markerPosition by remember { mutableStateOf<LatLng?>(null) }

    Scaffold { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            Column {
                SearchBar { query ->
                    performSearch(query = query, context = context) { results ->
                        if (results.isNotEmpty()) {
                            placeLocations = results
                            center = results.first()
                            cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(center, 12f))  // Aggiornamento del livello di zoom
                        }
                    }
                }
                MapView(center, placeLocations, cameraPositionState, {
                    showButton = true
                }, updateMarkerPosition = {
                    markerPosition = it
                },
                    state,
                    navController,
                    user)
            }

            if (showButton) {
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    onClick = {
                        navController.navigate(TravelDiaryRoute.HomeAddMark.buildRoute(
                            user.username,
                            markerPosition!!.latitude.toFloat(),
                            markerPosition!!.longitude.toFloat()))
                    },
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp, bottom = 32.dp)
                ) {
                    Row (modifier = Modifier.padding(8.dp)){
                        Icon(Icons.Outlined.Add, "Share Travel")
                        Text(text = "Aggiungi location")
                    }
                }
            }
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                onClick = {
                        onPosition()
                    },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 16.dp, top = 70.dp)
            ) {
                Text(
                    text = "Usa la tua posizione",
                    modifier = Modifier.padding(8.dp)
                )
            }
            DropMenu(user = user, navController = navController)
        }
    }

}

@Composable
fun MapView(
    center: LatLng,
    placeLocations: List<LatLng>,
    cameraPositionState: CameraPositionState,
    onMarkerClick: () -> Unit,
    updateMarkerPosition: (LatLng?) -> Unit,
    state : MarkersState,
    navController: NavHostController,
    user : User
) {
    var markerPosition by remember { mutableStateOf<LatLng?>(null) }  // Aggiunge lo stato per memorizzare la posizione del marker

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = { latLng ->
            markerPosition = latLng
            updateMarkerPosition(markerPosition)  // Aggiorna la posizione ogni volta che viene cliccato un nuovo punto
        }
    ) {
        // Visualizza il marker nella posizione memorizzata
        markerPosition?.let {
            Marker(
                state = MarkerState(position = it),
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)  // Imposta il marker di colore giallo
            )
            println(state.markers)
            onMarkerClick()
        }

        // Visualizza tutti i markers delle posizioni di ricerca
        placeLocations.forEach { location ->
            Marker(state = MarkerState(position = location))
        }

        state.markers.forEach{marker ->
            Marker(
                state = MarkerState(LatLng(marker.latitude.toDouble(), marker.longitude.toDouble())),
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
                onClick = {
                    navController.navigate(TravelDiaryRoute.HomeMarkDetail.buildRoute(
                        user.username,
                        marker.latitude,
                        marker.longitude
                        ))
                    true
                }
            )
        }
    }
}


@Composable
private fun SearchBar(onQueryChanged: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = {
            text = it
            onQueryChanged(it)
        },
        label = { Text("Cerca luogo") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        trailingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search")
        }
    )
}

private fun performSearch(query: String, context: Context, onResult: (List<LatLng>) -> Unit) {
    if (!Places.isInitialized()) {
        Places.initialize(context, "AIzaSyB6IrzeS3vCCiPHToNAG5u0tZkIyJx1IbM")
    }
    val placesClient = Places.createClient(context)
    val request = FindAutocompletePredictionsRequest.builder()
        .setQuery(query)
        .build()

    placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
        val locations = mutableListOf<LatLng>()
        for (prediction in response.autocompletePredictions) {
            fetchPlaceDetails(prediction.placeId, placesClient) { latLng ->
                latLng?.let {
                    locations.add(it)
                    if (locations.size == response.autocompletePredictions.size) {
                        onResult(locations)
                    }
                }
            }
        }
    }.addOnFailureListener { exception ->
        Log.e("SearchPlaces", "Error fetching autocomplete predictions", exception)
    }
}

private fun fetchPlaceDetails(placeId: String, placesClient: PlacesClient, onResult: (LatLng?) -> Unit) {
    val placeFields = listOf(Place.Field.LAT_LNG)
    val request = FetchPlaceRequest.newInstance(placeId, placeFields)

    placesClient.fetchPlace(request).addOnSuccessListener { fetchPlaceResponse ->
        onResult(fetchPlaceResponse.place.latLng)
    }.addOnFailureListener { exception ->
        Log.e("FetchPlaceDetails", "Error fetching place details", exception)
        onResult(null)
    }
}