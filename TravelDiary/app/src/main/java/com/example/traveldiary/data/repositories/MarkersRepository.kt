package com.example.traveldiary.data.repositories

import com.example.traveldiary.data.database.Marker
import com.example.traveldiary.data.database.PlacesDAO
import kotlinx.coroutines.flow.Flow

class MarkersRepository(private val placesDAO: PlacesDAO) {
    val markers: Flow<List<Marker>> = placesDAO.getAllMarker()

    suspend fun upsert(marker: Marker) = placesDAO.upsertMarker(marker)
}