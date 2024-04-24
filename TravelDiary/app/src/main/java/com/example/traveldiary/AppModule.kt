package com.example.traveldiary

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.traveldiary.data.database.TravelDiaryDatabase
import com.example.traveldiary.data.repositories.MarkersRepository
import com.example.traveldiary.data.repositories.UsersRepository
import com.example.traveldiary.ui.MarkersViewModel
import com.example.traveldiary.ui.UsersViewModel
import com.example.traveldiary.ui.screens.homeAddMark.AddMarkerViewModel
import com.example.traveldiary.ui.screens.login.LoginViewModel
import com.example.traveldiary.ui.screens.signin.AddTravelViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore("settings")

val appModule = module {
    single { get<Context>().dataStore }

    single {
        Room.databaseBuilder(
            get(),
            TravelDiaryDatabase::class.java,
            "travel-diary"
        ).build()
    }

    single { UsersRepository(get<TravelDiaryDatabase>().placesDAO()) }

    single { MarkersRepository(get<TravelDiaryDatabase>().placesDAO()) }

    viewModel { AddTravelViewModel() }

    viewModel { LoginViewModel() }

    viewModel { UsersViewModel(get()) }

    viewModel { MarkersViewModel(get()) }

    viewModel { AddMarkerViewModel() }
}
