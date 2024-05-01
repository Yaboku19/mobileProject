package com.example.traveldiary.ui.screens.homeMarks

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.traveldiary.data.database.Marker
import com.example.traveldiary.data.database.User
import com.example.traveldiary.ui.MarkersState
import com.example.traveldiary.ui.TravelDiaryRoute
import com.example.traveldiary.ui.composables.DropMenu
@Composable
fun HomeMarksScreen(navController: NavHostController, state : MarkersState, user: User) {
    var provinceFilter by remember { mutableStateOf("")}
    var cityFilter by remember { mutableStateOf("")}
    val setCity = mutableSetOf<String>()
    val setProvince = mutableSetOf<String>()
    state.markers.forEach{setCity.add(it.city)}
    state.markers.forEach{setProvince.add(it.province)}

    Scaffold(
        topBar = { InnerAppBar(
            setCity,
            setProvince,
            onSubmit = fun(city : String, province : String) {
                cityFilter = city
                provinceFilter = province
            },
            onReset = {
                cityFilter = ""
                provinceFilter = ""
            }
        ) }
    ) { contentPadding ->
        if (state.markers.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 80.dp),
                modifier = Modifier.padding(contentPadding)
            ) {
                items(state.markers) { item ->
                    if (
                        (provinceFilter == "" || item.province == provinceFilter)
                        &&
                        (cityFilter == "" || item.city == cityFilter)
                    ) {
                        TravelItem(
                            item,
                            onClick = {
                                navController.navigate(
                                    TravelDiaryRoute.HomeMarkDetail.buildRoute(
                                        user.username,
                                        item.latitude,
                                        item.longitude
                                    )
                                )
                            }
                        )
                    }
                }
            }
        } else {
            NoItemsPlaceholder(Modifier.padding(contentPadding))
        }
        DropMenu(user = user, navController = navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelItem(item: Marker, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .size(150.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                Icons.Outlined.Image,
                "Travel picture",
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary),
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(20.dp)
            )
            Spacer(Modifier.size(8.dp))
            Text(
                item.name,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun NoItemsPlaceholder(modifier: Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            Icons.Outlined.LocationOn, "Location icon",
            modifier = Modifier
                .padding(bottom = 16.dp)
                .size(48.dp),
            tint = MaterialTheme.colorScheme.secondary
        )
        Text(
            "No items",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InnerAppBar(setCity : Set<String>, setProvince : Set<String>, onSubmit : (String, String) -> Unit, onReset: () -> Unit) {
    var showMenu by remember { mutableStateOf(false) }
    var cityValue by remember { mutableStateOf("Città") }
    var cityMenu by remember { mutableStateOf(false) }
    var provinceValue by remember { mutableStateOf("Provincia") }
    var provinceMenu by remember { mutableStateOf(false) }
    CenterAlignedTopAppBar(
        title = {
            Text(text = "Filtri", fontSize = 18.sp, modifier = Modifier.padding(vertical = 4.dp))
        },
        actions = {
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(Icons.Filled.FilterList, contentDescription = "Filtra")
            }
        }
    )
    if(showMenu) {
        Box(
            modifier = Modifier
                .absoluteOffset(
                    x = 9000000.dp, // Posizione X (può richiedere calcoli)
                    y = 56.dp // Posizione Y (altezza della TopAppBar + padding)
                )
        ) {
            Column {
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        onClick = {cityMenu = true},
                        text = {
                            Text(cityValue)
                        }
                    )

                    DropdownMenuItem(
                        onClick = {provinceMenu = true},
                        text = { Text(provinceValue) }
                    )

                    DropdownMenuItem(
                        onClick = {
                            showMenu = false
                            onSubmit(
                                if(cityValue == "Città") "" else cityValue,
                                if(provinceValue == "Provincia") "" else provinceValue
                            )
                        },
                        text = { Text("fatto") }
                    )

                    DropdownMenuItem(
                        onClick = {
                            showMenu = false
                            onReset()
                            cityValue = "Città"
                            provinceValue = "Provincia"
                        },
                        text = { Text("resetta") }
                    )
                }
                InnerMenu(
                    expand = cityMenu,
                    onDismiss = { cityMenu = false },
                    setItem = setCity,
                    onSetItem = {
                        cityMenu = false
                        cityValue = it
                    }
                )

                InnerMenu(
                    expand = provinceMenu,
                    onDismiss = { provinceMenu = false },
                    setItem = setProvince,
                    onSetItem = {
                        provinceMenu = false
                        provinceValue = it
                    }
                )

            }

        }
    }
}

@Composable
private fun InnerMenu(expand : Boolean, onDismiss : () -> Unit, setItem : Set<String>, onSetItem : (String) -> Unit) {
    DropdownMenu(
        expanded = expand,
        onDismissRequest = { onDismiss() }
    ){
        setItem.forEach{
            if(it.isNotEmpty()) {
                DropdownMenuItem(
                    onClick = {
                        onSetItem(it)
                    },
                    text = { Text(text = it) }
                )
            }
        }
    }
}