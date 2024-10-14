package com.dicoding.dicodingevent.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.dicodingevent.SettingPreferences
import com.dicoding.dicodingevent.dataStore
import com.dicoding.dicodingevent.di.Injection
import com.dicoding.dicodingevent.repository.EventRepository
import com.dicoding.dicodingevent.ui.detail.DetailViewModel
import com.dicoding.dicodingevent.ui.favorite.FavoriteViewModel
import com.dicoding.dicodingevent.ui.finished.FinishedViewModel
import com.dicoding.dicodingevent.ui.home.HomeViewModel
import com.dicoding.dicodingevent.ui.upcoming.UpcomingViewModel

class ViewModelFactory private constructor(
    private val eventRepository: EventRepository,
    private val settingPreferences: SettingPreferences
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(eventRepository, settingPreferences) as T
            }

            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(eventRepository) as T
            }

            modelClass.isAssignableFrom(UpcomingViewModel::class.java) -> {
                UpcomingViewModel(eventRepository) as T
            }

            modelClass.isAssignableFrom(FinishedViewModel::class.java) -> {
                FinishedViewModel(eventRepository) as T
            }

            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(eventRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                val preferences = SettingPreferences.getInstance(context.dataStore)
                instance ?: ViewModelFactory(Injection.provideEventRepository(context), preferences)
            }.also { instance = it }
    }
}

