package com.dicoding.dicodingevent.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_detail")
class EventDetailEntity(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey
    val id: Int,

    @field:ColumnInfo(name = "title")
    val name: String,

    @field:ColumnInfo(name = "summary")
    val summary: String,

    @field:ColumnInfo(name = "description")
    val description: String,

    @field:ColumnInfo(name = "quota")
    val quota: String,

    @field:ColumnInfo(name = "registrants")
    val registrants: String,

    @field:ColumnInfo(name = "beginTime")
    val beginTime: String,

    @field:ColumnInfo(name = "urlToImage")
    val mediaCover: String,

    @field:ColumnInfo(name = "link")
    val link: String,

    @field:ColumnInfo(name = "favorited")
    var isFavorited: Boolean

)