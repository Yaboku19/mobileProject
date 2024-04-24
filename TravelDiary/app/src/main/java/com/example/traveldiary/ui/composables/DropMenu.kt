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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.traveldiary.data.database.User
import com.example.traveldiary.ui.TravelDiaryRoute

@Composable
fun DropMenu(user: User, navController: NavHostController) {
    var showMenu by remember { mutableStateOf(false) }
    Box() {
        FloatingActionButton(
            onClick = { showMenu = !showMenu },
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 70.dp, start = 16.dp)  // Posizionamento in alto a destra
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
                    navController.navigate(TravelDiaryRoute.HomeMap.buildRoute(user.username))
                },
                text = { Text("Mappa") }
            )

            DropdownMenuItem(
                onClick = {
                    showMenu = false
                    // Aggiungi azione per "Opzione 3"
                    println("Opzione 3 selezionata")
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