package com.example.traveldiary.ui.screens.homeMarkDetail

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    Box() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.TopStart,
                modifier = Modifier
                    .padding(16.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
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
            Button(onClick = {
                navController.navigate(
                    TravelDiaryRoute.HomeMap.buildRoute(
                        user.username,
                        marker.latitude,
                        marker.longitude
                    )
                )
            }) {
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
