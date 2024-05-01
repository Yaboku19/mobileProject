package com.example.traveldiary.ui.screens.homeProfile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.traveldiary.R
import com.example.traveldiary.data.database.User
import com.example.traveldiary.ui.composables.DropMenu
import com.example.traveldiary.utils.camera.CameraLauncher

@Composable
fun HomeProfileScreen(
    navController: NavHostController,
    user: User,
    onCamera : () -> Unit,
    cameraLauncher: CameraLauncher,
    onModify : (User) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (user.urlProfilePicture == "default.png") {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Profile Picture",
                modifier = Modifier.padding(16.dp)  // Aggiungi padding se necessario per styling
            )
        } else {
            val ctx = LocalContext.current
            AsyncImage(
                ImageRequest.Builder(ctx)
                    .data(user.urlProfilePicture.toUri())
                    .crossfade(true)
                    .build(),
                "Profile Picture"
            )
        }

        Button(onClick = onCamera) {
            Text("Take a Picture")
        }
    }
    if (cameraLauncher.capturedImageUri.path?.isNotEmpty() == true) {
        onModify(User(user.id, user.username, user.password, cameraLauncher.capturedImageUri.toString()))
    }
    DropMenu(user = user, navController = navController)
}