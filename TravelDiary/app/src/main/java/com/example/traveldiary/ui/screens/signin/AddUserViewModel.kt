package com.example.traveldiary.ui.screens.signin

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AddUserState(
    val username: String = "",
    val password: String = "",
    val salt: ByteArray = ByteArray(800)
) {
    val canSubmit get() = username.isNotBlank() && password.isNotBlank()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AddUserState

        if (username != other.username) return false
        if (password != other.password) return false
        return salt.contentEquals(other.salt)
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + salt.contentHashCode()
        return result
    }
}

interface AddUserActions {
    fun setUsername(username: String)
    fun setPassword(password: String)
    fun setSalt(byteArray: ByteArray)
}

class AddTravelViewModel : ViewModel() {
    private val _state = MutableStateFlow(AddUserState())
    val state = _state.asStateFlow()

    val actions = object : AddUserActions {
        override fun setUsername(username: String) =
            _state.update { it.copy(username = username) }

        override fun setPassword(password: String) =
            _state.update { it.copy(password = password) }

        override fun setSalt(byteArray: ByteArray) {
            _state.update { it.copy(salt = byteArray) }
        }
    }
}


