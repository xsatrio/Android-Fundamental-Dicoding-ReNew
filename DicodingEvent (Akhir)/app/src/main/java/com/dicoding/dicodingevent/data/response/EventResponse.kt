package com.dicoding.dicodingevent.data.response

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class EventResponse(

    @field:SerializedName("listEvents")
    @Json(name = "event")
    val listEvents: List<ListEventsItem> = listOf()
)

data class ListEventsItem(

    @field:SerializedName("summary")
    val summary: String,

    @field:SerializedName("mediaCover")
    val mediaCover: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("beginTime")
    val beginTime: String
)
