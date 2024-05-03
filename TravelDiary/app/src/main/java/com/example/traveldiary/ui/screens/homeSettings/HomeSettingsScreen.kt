package com.example.traveldiary.ui.screens.homeSettings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.traveldiary.R
import com.example.traveldiary.data.models.Theme
import com.example.traveldiary.ui.ThemeState
import com.example.traveldiary.ui.TravelDiaryRoute
import com.example.traveldiary.data.database.User
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import com.example.traveldiary.ui.composables.DropMenu
import androidx.compose.foundation.layout.fillMaxWidth

@Composable
fun HomeSettingsScreen(
    navController: NavHostController,
    user: User,
    state: ThemeState,
    onThemeSelected: (Theme) -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        LogOutButton(navController)
        Spacer(modifier = Modifier.height(10.dp))
        ThemeSelector(state, showMenu, onThemeSelected) { showMenu = it }
    }
    DropMenu(user = user, navController = navController)
}

@Composable
fun ThemeSelector(
    state: ThemeState,
    showMenu: Boolean,
    onThemeSelected: (Theme) -> Unit,
    setShowMenu: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Theme = " + stringResource(id = when (state.theme) {
                Theme.Light -> R.string.theme_light
                Theme.Dark -> R.string.theme_dark
                Theme.System -> R.string.theme_system
            }),
            fontSize = 20.sp,
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = { setShowMenu(!showMenu) },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Text(text = "Change Theme")
        }
        if(showMenu) {
            Box (
                modifier = Modifier
                    .absoluteOffset(
                        x = 9000000.dp, // Posizione X (può richiedere calcoli)
                        y = 56.dp // Posizione Y (altezza della TopAppBar + padding)
                    )
            ){
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { setShowMenu(false) }
                ) {
                    Theme.entries.forEach { theme ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringResource(
                                        id = when (theme) {
                                            Theme.Light -> R.string.theme_light
                                            Theme.Dark -> R.string.theme_dark
                                            Theme.System -> R.string.theme_system
                                        }
                                    )
                                )
                            },
                            onClick = {
                                setShowMenu(false)
                                onThemeSelected(theme)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LogOutButton(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Button(
            onClick = { navController.navigate(TravelDiaryRoute.LogIn.route) },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Text(text = "Log Out")
        }
    }
}
