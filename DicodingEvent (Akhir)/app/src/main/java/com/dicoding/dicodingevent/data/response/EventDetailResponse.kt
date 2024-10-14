package com.dicoding.dicodingevent.data.response

import com.google.gson.annotations.SerializedName

data class EventDetailResponse(

    @field:SerializedName("event")
    val event: Event
)

data class Event(

    @field:SerializedName("summary")
    val summary: String,

    @field:SerializedName("mediaCover")
    val mediaCover: String,

    @field:SerializedName("registrants")
    val registrants: Int,

    @field:SerializedName("link")
    val link: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("quota")
    val quota: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("beginTime")
    val beginTime: String
)
