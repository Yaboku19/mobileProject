package com.example.traveldiary.ui.screens.homeProfile

import android.Manifest
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.traveldiary.R
import com.example.traveldiary.data.database.User
import com.example.traveldiary.ui.TravelDiaryRoute
import com.example.traveldiary.ui.composables.DropMenu
import com.example.traveldiary.utils.camera.rememberCameraLauncher
import com.example.traveldiary.utils.position.rememberPermission

@Composable
fun HomeProfileScreen(
    navController: NavHostController,
    user: User,
    onModify: (User) -> Unit,
    state: HomeProfileState,
    actions: HomeProfileActions
) {
    val ctx = LocalContext.current

    val cameraLauncher = rememberCameraLauncher()

    val cameraPermission = rememberPermission(Manifest.permission.CAMERA) { status ->
        if (status.isGranted) {
            cameraLauncher.captureImage()
        } else {
            Toast.makeText(ctx, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    fun takePicture() {
        if (cameraPermission.status.isGranted) {
            cameraLauncher.captureImage()
        } else {
            cameraPermission.launchPermissionRequest()
        }
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
                modifier = Modifier
                    .padding(16.dp)
                    .size(200.dp)  // Adjust the size to be about 1/4th and manageable
                    .clip(CircleShape)  // Apply a circular clip to the image
            )
        } else {
            AsyncImage(
                model = user.urlProfilePicture.toUri(),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(200.dp)  // Adjust the size to be about 1/4th and manageable
                    .clip(CircleShape)  // Apply a circular clip to the image
            )
        }

        Button(
            onClick = { takePicture() },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Text("Fai una foto")
        }
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = state.username,
                onValueChange = actions::setUsername,
                label = { Text("Username") },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    if (!state.canSubmitUser) return@Button
                    onModify(User(user.id, state.username, user.password, user.urlProfilePicture))
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.padding(start = 8.dp).align(Alignment.CenterVertically)
            ) {
                Text(text = "Cambia Username")
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start)
        ) {
            OutlinedTextField(
                value = state.password,
                onValueChange = actions::setPassword,
                label = { Text("Password") },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    if (!state.canSubmitPassword) return@Button
                    onModify(User(user.id, user.username, state.password, user.urlProfilePicture))
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.padding(start = 8.dp).align(Alignment.CenterVertically)
            ) {
                Text(text = "Cambia Password")
            }
        }


    }

    // Update the user's profile picture if a new picture is taken
    if (cameraLauncher.capturedImageUri.path?.isNotEmpty() == true) {
        onModify(User(user.id, user.username, user.password, cameraLauncher.capturedImageUri.toString()))
    }
    DropMenu(user = user, navController = navController)
}
