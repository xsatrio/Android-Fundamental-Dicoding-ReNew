package com.dicoding.dicodingevent.di

import android.content.Context
import com.dicoding.dicodingevent.data.retrofit.ApiConfig
import com.dicoding.dicodingevent.database.EventDatabase
import com.dicoding.dicodingevent.repository.EventRepository

object Injection {
    fun provideEventRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val dao = database.eventDao()
        return EventRepository.getInstance(apiService, dao)
    }
}
