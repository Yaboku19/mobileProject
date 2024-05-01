package com.example.traveldiary.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo
    var username: String,

    @ColumnInfo
    var password: String,

    @ColumnInfo
    var urlProfilePicture : String
)

@Entity
data class Marker (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo
    var name: String,

    @ColumnInfo
    var description: String,

    @ColumnInfo
    var city: String,

    @ColumnInfo
    var province: String,

    @ColumnInfo
    var latitude: Float,

    @ColumnInfo
    var longitude: Float
)

@Entity(primaryKeys = ["userId", "markerId"])
data class Favorite (
    @ColumnInfo
    var userId : Int,

    @ColumnInfo
    var markerId : Int
)
