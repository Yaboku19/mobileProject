package com.example.traveldiary.ui.screens.homeProfile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.traveldiary.data.database.User
import com.example.traveldiary.ui.composables.DropMenu

@Composable
fun HomeProfileScreen(navController: NavHostController, user: User, onCamera : () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = onCamera) {
            Text("Take a Picture")
        }
    }
    DropMenu(user = user, navController = navController)
}