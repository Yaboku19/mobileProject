package com.example.traveldiary.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.traveldiary.data.database.Marker
import com.example.traveldiary.data.database.User
import com.example.traveldiary.data.repositories.MarkersRepository
import com.example.traveldiary.data.repositories.UsersRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MarkersState(val markers: List<Marker>)

class MarkersViewModel(
    private val repository: MarkersRepository
) : ViewModel() {

    val state = repository.markers.map { MarkersState(markers = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = MarkersState(emptyList())
    )

    fun addMarker(marker: Marker) {
        viewModelScope.launch {
            repository.upsert(marker)
        }
    }

    fun getMarker (latitude: Float, longitude: Float) {
        viewModelScope.launch {

        }
    }

    fun deleteMarker(marker: Marker) = viewModelScope.launch {
        repository.delete(marker)
    }
}