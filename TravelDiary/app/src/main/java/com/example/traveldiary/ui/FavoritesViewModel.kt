package com.example.traveldiary.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.traveldiary.data.database.Favorite
import com.example.traveldiary.data.repositories.FavoritesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FavoritesState(val favorites: List<Favorite>)

class FavoritesViewModel(
    private val repository: FavoritesRepository
) : ViewModel() {
    val state = repository.favorites.map { FavoritesState(favorites = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = FavoritesState(emptyList())
    )

    fun addFavorite(favorite: Favorite) {
        viewModelScope.launch {
            repository.upsert(favorite)
        }
    }

    fun deleteFavorite(favorite: Favorite) {
        viewModelScope.launch {
            repository.delete(favorite)
        }
    }

}