package com.example.traveldiary.ui.screens.login

import androidx.lifecycle.ViewModel
import com.example.traveldiary.data.database.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LoginState(
    val username: String = "",
    val password: String = ""
) {
    val canSubmit get() = username.isNotBlank() && password.isNotBlank()

    fun toUser() = User(
        username = username,
        password = password,
        urlProfilePicture = "default.png"
    )
}

interface LoginActions {
    fun setUsername(title: String)
    fun setPassword(date: String)
}

class LoginViewModel : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    val actions = object : LoginActions {
        override fun setUsername(title: String) =
            _state.update { it.copy(username = title) }

        override fun setPassword(date: String) =
            _state.update { it.copy(password = date) }
    }
}