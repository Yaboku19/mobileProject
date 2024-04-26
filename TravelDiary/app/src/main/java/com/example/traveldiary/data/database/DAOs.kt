package com.example.traveldiary.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PlacesDAO {
    @Query("SELECT * FROM user ORDER BY username ASC")
    fun getAllUser(): Flow<List<User>>

    @Query("SELECT * FROM marker ORDER BY name ASC")
    fun getAllMarker(): Flow<List<Marker>>

    @Query("SELECT * FROM user WHERE username = :user")
    fun getUser(user: String): Flow<User?>

    @Query("SELECT * FROM marker WHERE latitude = :latitude AND longitude = :longitude")
    fun getMarker(latitude: Float, longitude: Float): Flow<Marker?>

    @Upsert
    suspend fun upsertUser(user: User)

    @Upsert
    suspend fun upsertMarker(marker: Marker)

    @Delete
    suspend fun deleteUser(item: User)

    @Delete
    suspend fun deleteMarker(item: Marker)
}
