package com.dicoding.dicodingevent.data.retrofit

import com.dicoding.dicodingevent.data.response.EventDetailResponse
import com.dicoding.dicodingevent.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("events")
    fun listEvent(
        @Query("active") query: String
    ): Call<EventResponse>

    @GET("events/{id}")
    fun getDetailEvent(
        @Path("id") id: Int
    ): Call<EventDetailResponse>

    @GET("events")
    fun searchEvent(
        @Query("active") active: Int,
        @Query("q") keyword: String
    ): Call<EventResponse>
}
