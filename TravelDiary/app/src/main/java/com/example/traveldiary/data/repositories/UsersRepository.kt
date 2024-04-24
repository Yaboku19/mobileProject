package com.example.traveldiary.data.repositories

import com.example.traveldiary.data.database.PlacesDAO
import com.example.traveldiary.data.database.User
import kotlinx.coroutines.flow.Flow

class UsersRepository(private val placesDAO: PlacesDAO) {
    val users: Flow<List<User>> = placesDAO.getAllUser()

    suspend fun upsert(user: User) = placesDAO.upsertUser(user)

    suspend fun delete(user: User) = placesDAO.deleteUser(user)

    fun getUser(user: String) = placesDAO.getUser(user)
}
