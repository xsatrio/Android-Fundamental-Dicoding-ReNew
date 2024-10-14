package com.dicoding.dicodingevent.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_finished")
class EventFinishedEntity(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey
    val id: Int,

    @field:ColumnInfo(name = "title")
    val name: String,

    @field:ColumnInfo(name = "summary")
    val summary: String,

    @field:ColumnInfo(name = "urlToImage")
    val mediaCover: String? = null
)