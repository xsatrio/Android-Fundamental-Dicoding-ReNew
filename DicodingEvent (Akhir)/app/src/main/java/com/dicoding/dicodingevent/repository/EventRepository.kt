package com.dicoding.dicodingevent.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.dicoding.dicodingevent.data.Results
import com.dicoding.dicodingevent.data.retrofit.ApiService
import com.dicoding.dicodingevent.database.EventDao
import com.dicoding.dicodingevent.database.entity.EventActiveEntity
import com.dicoding.dicodingevent.database.entity.EventDetailEntity
import com.dicoding.dicodingevent.database.entity.EventFinishedEntity

class EventRepository(
    private val apiService: ApiService,
    private val eventDao: EventDao
) {

    fun getEventActive(): LiveData<Results<List<EventActiveEntity>>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiService.listEvent("1")
            val events = response.listEvents
            val eventActiveList = events.map { event ->
                EventActiveEntity(
                    event.id,
                    event.name,
                    event.summary,
                    event.beginTime,
                    event.mediaCover
                )
            }
            eventDao.deleteAllActive()
            eventDao.insertEventActive(eventActiveList)
        } catch (e: Exception) {
            Log.d("EventRepository", "getEventActive: ${e.message.toString()} ")
            emit(Results.Error(e.message.toString()))
        }
        val localData: LiveData<Results<List<EventActiveEntity>>> =
            eventDao.getEventActive().map { Results.Success(it) }
        emitSource(localData)
    }

    fun getEventFinished(): LiveData<Results<List<EventFinishedEntity>>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiService.listEvent("0")
            val events = response.listEvents
            val eventFinishedList = events.map { event ->
                EventFinishedEntity(
                    event.id,
                    event.name,
                    event.summary,
                    event.mediaCover
                )
            }
            eventDao.deleteAllFinished()
            eventDao.insertEventFinished(eventFinishedList)
        } catch (e: Exception) {
            Log.d("EventRepository", "getEventActive: ${e.message.toString()} ")
            emit(Results.Error(e.message.toString()))
        }
        val localData: LiveData<Results<List<EventFinishedEntity>>> =
            eventDao.getEventFinished().map { Results.Success(it) }
        emitSource(localData)
    }

    fun getEventDetail(eventId: Int): LiveData<Results<EventDetailEntity>> = liveData {
        emit(Results.Loading)
        try {
            val response = apiService.getDetailEvent(eventId)
            val eventsDetail = response.event
            val eventDetail = eventsDetail.let { detail ->
                val isFavorited = eventDao.isEventFavorite(detail.id)
                EventDetailEntity(
                    detail.id,
                    detail.name,
                    detail.summary,
                    detail.description,
                    detail.quota.toString(),
                    detail.registrants.toString(),
                    detail.beginTime,
                    detail.mediaCover,
                    detail.link,
                    isFavorited
                )
            }
            eventDao.updateEventDetail(eventDetail)
            eventDao.insertEventDetail(eventDetail)
        } catch (e: Exception) {
            Log.d("EventRepository", "getEventDetail: ${e.message.toString()}")
            emit(Results.Error(e.message.toString()))
            return@liveData
        }
        val localData: LiveData<Results<EventDetailEntity>> =
            eventDao.getEventDetail(eventId).map { Results.Success(it) }
        emitSource(localData)
    }

    suspend fun setEventFavorite(event: EventDetailEntity, favoriteState: Boolean) {
        event.isFavorited = favoriteState
        eventDao.updateEventDetail(event)
    }

    fun getEventFavorited(): LiveData<Results<List<EventDetailEntity>>> = liveData {
        emit(Results.Loading)
        try {
            val localData: LiveData<Results<List<EventDetailEntity>>> =
                eventDao.getFavoritedEvents().map { Results.Success(it) }
            emitSource(localData)
        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            eventDao: EventDao
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, eventDao)
            }.also { instance = it }
    }
}
