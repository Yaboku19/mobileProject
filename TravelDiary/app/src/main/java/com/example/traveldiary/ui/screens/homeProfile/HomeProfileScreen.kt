package com.example.traveldiary.ui.screens.homeProfile

import android.Manifest
import android.widget.Toast
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
import com.example.traveldiary.utils.camera.rememberCameraLauncher
import com.example.traveldiary.utils.position.rememberPermission

@Composable
fun HomeProfileScreen(
    navController: NavHostController,
    user: User,
    onModify : (User) -> Unit
) {
    val ctx = LocalContext.current

    val cameraLauncheri = rememberCameraLauncher()

    val cameraPermission = rememberPermission(Manifest.permission.CAMERA) { status ->
        if (status.isGranted) {
            cameraLauncheri.captureImage()
        } else {
            Toast.makeText(ctx, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    fun takePicture() =
        if (cameraPermission.status.isGranted) {
            cameraLauncheri.captureImage()
        } else {
            cameraPermission.launchPermissionRequest()
        }
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
            AsyncImage(
                ImageRequest.Builder(ctx)
                    .data(user.urlProfilePicture.toUri())
                    .crossfade(true)
                    .build(),
                "Profile Picture"
            )
        }

        Button(onClick = { takePicture() }) {
            Text("Take a Picture")
        }
    }
    if (cameraLauncheri.capturedImageUri.path?.isNotEmpty() == true) {
        onModify(User(user.id, user.username, user.password, cameraLauncheri.capturedImageUri.toString()))
    }
    DropMenu(user = user, navController = navController)
}