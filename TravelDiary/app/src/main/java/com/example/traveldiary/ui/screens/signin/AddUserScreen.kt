package com.example.traveldiary.ui.screens.signin

import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.traveldiary.data.database.User
import com.example.traveldiary.ui.TravelDiaryRoute
import com.example.traveldiary.ui.UsersViewModel
import java.security.SecureRandom

@Composable
fun AddUserScreen(
    state: AddUserState,
    actions: AddUserActions,
    onSubmit: (User) -> Unit,
    navController: NavHostController,
    viewModel: UsersViewModel
) {
    val addUserResult by viewModel.addUserResult.observeAsState()
    val addUserLog by viewModel.addUserLog.observeAsState()

    Scaffold { contentPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(contentPadding)
                .padding(12.dp)
                .fillMaxSize()
        ) {

            OutlinedTextField(
                value = state.username,
                onValueChange = actions::setUsername,
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.password,
                onValueChange = actions::setPassword,
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.size(24.dp))
            Button(
                onClick = {
                    if (!state.canSubmit) return@Button
                    val random = SecureRandom()
                    val num = random.nextInt() % 18
                    val salt = viewModel.generateSalt(num)
                    val password = viewModel.hashPassword(state.password, salt)
                    onSubmit(User(username = state.username, password = password, salt = salt, urlProfilePicture = "default.png"))
                },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Icon(
                    Icons.Outlined.DoneOutline,
                    contentDescription = "done icon",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("sign-in")
            }
            if (addUserResult == false) {
                Text(addUserLog.toString(), color = Color.Red)
            } else if (addUserResult == true) {
                navController.navigate(TravelDiaryRoute.LogIn.route)
            }
        }
    }
}
