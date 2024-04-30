package com.example.traveldiary.ui.screens.homeSettings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.traveldiary.R
import com.example.traveldiary.data.database.User
import com.example.traveldiary.data.models.Theme
import com.example.traveldiary.ui.ThemeState
import com.example.traveldiary.ui.TravelDiaryRoute

@Composable
fun HomeSettingsScreen(
    navHostController: NavHostController,
    user: User,
    state: ThemeState,
    onThemeSelected: (Theme) -> Unit
) {
    Column(Modifier.selectableGroup()) {
        Theme.entries.forEach { theme ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (theme == state.theme),
                        onClick = { onThemeSelected(theme) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (theme == state.theme),
                    onClick = null
                )
                Text(
                    text = stringResource(when (theme) {
                        Theme.Light -> R.string.theme_light
                        Theme.Dark -> R.string.theme_dark
                        Theme.System -> R.string.theme_system
                    }),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }

        Button(onClick = { navHostController.navigate(TravelDiaryRoute.LogIn.route) }) {
            Text(text = "log-out")
        }
    }

}