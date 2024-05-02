package com.example.traveldiary.ui.screens.homeAddMark

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DoneOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.traveldiary.data.database.User
import okhttp3.OkHttpClient
import okhttp3.Request
import androidx.compose.runtime.*
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun HomeAddMarkScreen(
    state: AddMarkerState,
    actions: AddMarkerActions,
    navController: NavHostController,
    latitude : Float,
    longitude : Float,
    user: User,
    onSubmit: () -> Unit,
) {
    var locationDetails by remember { mutableStateOf(false) }

    LaunchedEffect(latitude, longitude) {  // Assicurati di ricaricare i dati quando le coordinate cambiano
        parseLocationData(fetchLocationDetails(latitude, longitude, "AIzaSyB6IrzeS3vCCiPHToNAG5u0tZkIyJx1IbM"), actions)
        locationDetails = true
    }

    Scaffold { contentPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(contentPadding)
                .padding(12.dp)
                .fillMaxSize()
        ) {
            actions.setLatitude(latitude)
            actions.setLongitude(longitude)
            OutlinedTextField(
                value = state.name,
                onValueChange = actions::setName,
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.description,
                onValueChange = actions::setDescription,
                label = { Text("Descrizione") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.size(24.dp))
            Button(
                onClick = {
                    if (!state.canSubmit) return@Button
                    if (locationDetails) {
                        onSubmit()
                        navController.navigateUp()
                    }

                },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Icon(
                    Icons.Outlined.DoneOutline,
                    contentDescription = "done icon",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Aggiungi posto")
            }
        }
    }
}


private suspend fun fetchLocationDetails(latitude: Float, longitude: Float, apiKey: String) : String {
    val httpClient = OkHttpClient()
    val url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=$latitude,$longitude&key=$apiKey"

    return withContext(Dispatchers.IO) {  // esegue questo blocco in un thread di I/O
        val request = Request.Builder()
            .url(url)
            .build()

        httpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                "Server error: ${response.code}"
            } else {
                response.body?.string() ?: "Errore nella lettura della risposta"
            }
        }
    }
}

private fun parseLocationData(jsonResponse: String, actions: AddMarkerActions) {
    val jsonElement = JsonParser.parseString(jsonResponse)
    val results = jsonElement.asJsonObject.get("results").asJsonArray

    if (results.size() > 0) {
        val addressComponents = results.first().asJsonObject.get("address_components").asJsonArray

        val city = addressComponents.find {
            it.asJsonObject.get("types").asJsonArray.contains(JsonPrimitive("locality"))
        }?.asJsonObject?.get("long_name")?.asString

        val province = addressComponents.find {
            it.asJsonObject.get("types").asJsonArray.contains(JsonPrimitive("administrative_area_level_2"))
        }?.asJsonObject?.get("long_name")?.asString

        actions.setProvince(province ?: "")
        actions.setCity(city ?: "")
    } else {
        actions.setProvince("")
        actions.setCity("")
    }
}
