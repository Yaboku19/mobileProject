package com.example.traveldiary.ui.composables



import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.traveldiary.data.database.User
import com.example.traveldiary.ui.TravelDiaryRoute

@Composable
fun DropMenu(user: User, navController: NavHostController) {
    var showMenu by remember { mutableStateOf(false) }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute by remember {
        derivedStateOf {
            TravelDiaryRoute.routes.find {
                it.route == backStackEntry?.destination?.route
            } ?: TravelDiaryRoute.LogIn
        }
    }
    Box() {
        FloatingActionButton(
            onClick = { showMenu = !showMenu },
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = if(currentRoute.title == "homePage") 70.dp else 16.dp, start = 16.dp)  // Posizionamento in alto a destra
        ) {
            Icon(Icons.Filled.Menu, contentDescription = "Menu")
        }
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    showMenu = false
                    // Aggiungi azione per "Opzione 2"
                    println("Opzione 2 selezionata")
                },
                text = { Text("Profilo") }
            )
            DropdownMenuItem(
                onClick = {
                    showMenu = false
                    navController.navigate(TravelDiaryRoute.HomeMap.buildRoute(user.username, 41.9028f, 12.4964f))
                },
                text = { Text("Mappa") }
            )

            DropdownMenuItem(
                onClick = {
                    showMenu = false
                    navController.navigate(TravelDiaryRoute.HomeMarks.buildRoute(user.username))
                },
                text = { Text("Posizioni") }
            )
            DropdownMenuItem(
                onClick = {
                    showMenu = false
                    // Aggiungi azione per "Opzione 2"
                    println("Opzione 4 selezionata")
                },
                text = { Text("Preferiti") }
            )
            DropdownMenuItem(
                onClick = {
                    showMenu = false
                    // Aggiungi azione per "Opzione 2"
                    println("Opzione 4 selezionata")
                },
                text = { Text("Impostazioni") }
            )
        }
    }
}