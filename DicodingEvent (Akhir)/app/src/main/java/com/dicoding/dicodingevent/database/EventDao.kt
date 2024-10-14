package com.dicoding.dicodingevent.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dicoding.dicodingevent.database.entity.EventActiveEntity
import com.dicoding.dicodingevent.database.entity.EventDetailEntity
import com.dicoding.dicodingevent.database.entity.EventFinishedEntity

@Dao
interface EventDao {
    @Query("SELECT * FROM event_active")
    fun getEventActive(): LiveData<List<EventActiveEntity>>

    @Query("SELECT * FROM event_finished")
    fun getEventFinished(): LiveData<List<EventFinishedEntity>>

    @Query("SELECT * FROM event_detail WHERE favorited = 1")
    fun getFavoritedEvents(): LiveData<List<EventDetailEntity>>

    @Query("SELECT * FROM event_detail WHERE id = :eventId")
    fun getEventDetail(eventId: Int): LiveData<EventDetailEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEventActive(news: List<EventActiveEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEventFinished(event: List<EventFinishedEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEventDetail(event: EventDetailEntity)

    @Query("DELETE FROM event_active")
    suspend fun deleteAllActive()

    @Query("DELETE FROM event_finished")
    suspend fun deleteAllFinished()

    @Query("DELETE FROM event_detail WHERE favorited = 0")
    suspend fun deleteAllDetail()

    @Query("SELECT EXISTS(SELECT * FROM event_detail WHERE id = :id AND favorited = 1)")
    suspend fun isEventFavorite(id: Int): Boolean

    @Update
    suspend fun updateEventDetail(event: EventDetailEntity)

}
