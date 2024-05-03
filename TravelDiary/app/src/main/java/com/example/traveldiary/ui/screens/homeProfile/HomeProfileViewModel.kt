package com.example.traveldiary.ui.screens.homeProfile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class HomeProfileState(
    val username: String = "",
    val password: String = ""
) {
    val canSubmitUser get() = username.isNotBlank()
    val canSubmitPassword get() = password.isNotBlank()

}

interface HomeProfileActions {
    fun setUsername(title: String)
    fun setPassword(date: String)
}

class HomeProfileViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeProfileState())
    val state = _state.asStateFlow()

    val actions = object : HomeProfileActions {
        override fun setUsername(title: String) =
            _state.update { it.copy(username = title) }

        override fun setPassword(date: String) =
            _state.update { it.copy(password = date) }
    }
}