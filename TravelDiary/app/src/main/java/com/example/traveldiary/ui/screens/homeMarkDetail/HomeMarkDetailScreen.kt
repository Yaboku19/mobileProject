package com.example.traveldiary.ui.screens.homeMarkDetail

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.traveldiary.data.database.Marker
import com.example.traveldiary.data.database.User
import com.example.traveldiary.ui.TravelDiaryRoute

@Composable
fun HomeMarkDetailScreen(
    navController: NavHostController,
    marker: Marker,
    user: User,
    favorite : Boolean,
    onFavorite : (Int, Int) -> Unit,
    onSFavorite : (Int, Int) -> Unit
) {
    var isFilled by remember { mutableStateOf(favorite) }
    val ctx = LocalContext.current

    fun shareDetails() {

        val mapUrl = "https://www.google.com/maps?q=${marker.latitude},${marker.longitude}"

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                "name = ${marker.name} \n" +
                        "Descrizione = ${marker.description} \n" +
                        "Città = ${if(marker.city == "") "nessuna" else marker.city}\n" +
                        "Provincia = ${if (marker.province == "") "nessuna" else marker.province}\n" +
                        "Google Maps: $mapUrl"
            )
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share marker")
        if (shareIntent.resolveActivity(ctx.packageManager) != null) {
            ctx.startActivity(shareIntent)
        }
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                onClick = ::shareDetails
            ) {
                Icon(Icons.Outlined.Share, "Share Travel")
            }
        },
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.size(20.dp))
                Box(
                    contentAlignment = Alignment.TopStart,
                    modifier = Modifier
                        .padding(16.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()  // Assicura che la Column occupi tutto lo spazio orizzontale del Box
                    ) {
                        Text(
                            text = "Name:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp,
                            modifier = Modifier
                                .fillMaxWidth()  // Assicura che il Text occupi tutto lo spazio orizzontale della Column
                                .wrapContentWidth(Alignment.CenterHorizontally)  // Centra il Text all'interno della sua area
                        )
                        Text(
                            text = marker.name,
                            fontSize = 18.sp
                            // Questo Text è allineato a sinistra per default
                        )
                    }
                }
                Box(
                    contentAlignment = Alignment.TopStart,
                    modifier = Modifier
                        .padding(16.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()  // Assicura che la Column occupi tutto lo spazio orizzontale del Box
                    ) {
                        Text(
                            text = "Descrizione:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp,
                            modifier = Modifier
                                .fillMaxWidth()  // Assicura che il Text occupi tutto lo spazio orizzontale della Column
                                .wrapContentWidth(Alignment.CenterHorizontally)  // Centra il Text all'interno della sua area
                        )
                        Text(
                            text = marker.description,
                            fontSize = 18.sp
                            // Questo Text è allineato a sinistra per default
                        )
                    }
                }
                Box(
                    contentAlignment = Alignment.TopStart,
                    modifier = Modifier
                        .padding(16.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()  // Assicura che la Column occupi tutto lo spazio orizzontale del Box
                    ) {
                        Text(
                            text = "Città:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp,
                            modifier = Modifier
                                .fillMaxWidth()  // Assicura che il Text occupi tutto lo spazio orizzontale della Column
                                .wrapContentWidth(Alignment.CenterHorizontally)  // Centra il Text all'interno della sua area
                        )
                        Text(
                            text = marker.city,
                            fontSize = 18.sp
                            // Questo Text è allineato a sinistra per default
                        )
                    }
                }
                Box(
                    contentAlignment = Alignment.TopStart,
                    modifier = Modifier
                        .padding(16.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()  // Assicura che la Column occupi tutto lo spazio orizzontale del Box
                    ) {
                        Text(
                            text = "Provincia:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp,
                            modifier = Modifier
                                .fillMaxWidth()  // Assicura che il Text occupi tutto lo spazio orizzontale della Column
                                .wrapContentWidth(Alignment.CenterHorizontally)  // Centra il Text all'interno della sua area
                        )
                        Text(
                            text = marker.province,
                            fontSize = 18.sp
                        )
                    }
                }
                Button(
                    onClick = {
                        navController.navigate(
                            TravelDiaryRoute.HomeMap.buildRoute(
                                user.username,
                                marker.latitude,
                                marker.longitude
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Text(text = "Guarda posizione")
                }


            }

            IconButton(
                onClick = {
                    isFilled = if (isFilled) {
                        onSFavorite(user.id, marker.id)
                        false
                    } else {
                        onFavorite(user.id, marker.id)
                        true
                    }
                },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = if (isFilled) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = if (isFilled) "Unfavorite" else "Favorite",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
