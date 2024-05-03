package com.example.traveldiary.data.repositories

import com.example.traveldiary.data.database.Favorite
import com.example.traveldiary.data.database.PlacesDAO
import kotlinx.coroutines.flow.Flow

class FavoritesRepository(private val placesDAO: PlacesDAO) {
    val favorites: Flow<List<Favorite>> = placesDAO.getAllFavorite()

    suspend fun upsert(favorite: Favorite) = placesDAO.upsertFavorite(favorite)

    suspend fun delete(favorite: Favorite) = placesDAO.deleteFavorite(favorite)

}