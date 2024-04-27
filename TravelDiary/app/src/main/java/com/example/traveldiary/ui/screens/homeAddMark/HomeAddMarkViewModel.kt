package com.example.traveldiary.ui.screens.homeAddMark

import androidx.lifecycle.ViewModel
import com.example.traveldiary.data.database.Marker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AddMarkerState(
    val name: String = "",
    val description: String = "",
    val latitude: Float = 0f,
    val longitude: Float = 0f,
    val city : String = "",
    val province : String = ""
) {
    val canSubmit get() = name.isNotBlank() && description.isNotBlank()

    fun toMarker() = Marker(
        name = name,
        description = description,
        latitude = latitude,
        longitude = longitude,
        city = city,
        province = province
    )
}

interface AddMarkerActions {
    fun setName(name: String)
    fun setDescription(description: String)
    fun setCity(city : String)
    fun setProvince(province: String)
    fun setLatitude(latitude: Float)
    fun setLongitude(longitude: Float)
}

class AddMarkerViewModel : ViewModel() {
    private val _state = MutableStateFlow(AddMarkerState())
    val state = _state.asStateFlow()

    val actions = object : AddMarkerActions {
        override fun setName(name: String) =
            _state.update { it.copy(name = name) }

        override fun setDescription(description: String) =
            _state.update { it.copy(description = description) }

        override fun setCity(city: String) {
            _state.update { it.copy(city = city) }
        }

        override fun setProvince(province: String) {
            _state.update { it.copy(province = province) }
        }

        override fun setLatitude(latitude: Float) {
            _state.update { it.copy(latitude = latitude) }
        }

        override fun setLongitude(longitude: Float) {
            _state.update { it.copy(longitude = longitude) }
        }
    }
}