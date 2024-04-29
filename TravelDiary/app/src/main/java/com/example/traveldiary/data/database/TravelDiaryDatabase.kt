package com.example.traveldiary.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class, Marker::class, Favorite::class], version = 1)
abstract class TravelDiaryDatabase : RoomDatabase() {
    abstract fun placesDAO(): PlacesDAO
}
