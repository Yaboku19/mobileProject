package com.example.traveldiary.ui.screens.homeStatistics

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.traveldiary.data.database.User
import com.example.traveldiary.ui.MarkersState
import com.example.traveldiary.ui.composables.DropMenu
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate

@Composable
fun HomeStatisticsScreen(
    markerState: MarkersState,
    user: User,
    navController: NavHostController,
    favoriteState: MarkersState
) {
    var showFavorite by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { InnerAppBar(showFavorite) { showFavorite = it } }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            val currentState = if(showFavorite) favoriteState else markerState
            BarChartView(Modifier.weight(1f).fillMaxWidth(), currentState, true)
            BarChartView(Modifier.weight(1f).fillMaxWidth(), currentState, false)
        }
    }
    DropMenu(user = user, navController = navController)
}

@Composable
fun BarChartView(modifier: Modifier = Modifier, state: MarkersState, isCity: Boolean) {
    val context = LocalContext.current
    val (labelMap, labels) = prepareChartData(state, isCity)

    AndroidView(
        modifier = modifier,
        factory = {
            BarChart(context).also { barChart ->
                setupBarChart(barChart, labels, labelMap, isCity)
            }
        },
        update = { barChart ->
            // Forza il grafico a rileggere i dati e aggiornarsi
            setupBarChart(barChart, labels, labelMap, isCity)
        }
    )
}

fun prepareChartData(state: MarkersState, isCity: Boolean): Pair<Map<String, Int>, List<String>> {
    val labelMap = mutableMapOf<String, Int>()
    val labels = mutableListOf<String>()

    state.markers.forEach { marker ->
        val key = if (isCity) marker.city else marker.province
        key?.let {
            labelMap[it] = labelMap.getOrDefault(it, 0) + 1
            if (!labels.contains(it)) labels.add(it)
        }
    }

    labels.sort()
    return labelMap to labels
}

fun setupBarChart(barChart: BarChart, labels: List<String>, labelMap: Map<String, Int>, isCity: Boolean) {
    val sortedEntries = labels.mapIndexed { index, label ->
        BarEntry(index.toFloat(), labelMap[label]?.toFloat() ?: 0f)
    }
    val dataSet = BarDataSet(sortedEntries, if (isCity) "City" else "Province").apply {
        colors = ColorTemplate.COLORFUL_COLORS.toList()
        valueTextColor = android.graphics.Color.BLACK
        valueTextSize = 16f
    }
    barChart.data = BarData(dataSet)
    barChart.description.text = if (isCity) "Markers per City" else "Markers per Province"

    barChart.xAxis.apply {
        position = XAxis.XAxisPosition.BOTTOM
        valueFormatter = IndexAxisValueFormatter(labels)
        setLabelCount(labels.size, false)
        isGranularityEnabled = true
        granularity = 1f
        setAvoidFirstLastClipping(true)
    }

    barChart.animateY(2000)
    barChart.notifyDataSetChanged()
    barChart.invalidate()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InnerAppBar(isFavorite: Boolean, onSubmit: (Boolean) -> Unit) {
    var showMenu by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = { Text(text = "Grafici", fontSize = 18.sp, modifier = Modifier.padding(vertical = 4.dp)) },
        actions = {
            Button(onClick = { showMenu = !showMenu }) {
                Text(text = if(isFavorite) "Preferiti" else "Tutti")
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    onClick = {
                        showMenu = false
                        onSubmit(false)
                    },
                    text = { Text("Tutti") }
                )
                DropdownMenuItem(
                    onClick = {
                        showMenu = false
                        onSubmit(true)
                    },
                    text = { Text("Preferiti") }
                )
            }
        }
    )
}

