package com.dicoding.dicodingevent.data.retrofit

import com.dicoding.dicodingevent.data.response.EventDetailResponse
import com.dicoding.dicodingevent.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("events")
    suspend fun listEvent(
        @Query("active") query: String
    ): EventResponse

    @GET("events/{id}")
    suspend fun getDetailEvent(
        @Path("id") id: Int
    ): EventDetailResponse

    @GET("events")
    fun searchEvent(
        @Query("active") active: Int,
        @Query("q") keyword: String
    ): Call<EventResponse>

    @GET("events")
    suspend fun getClosestEvent(
        @Query("active") active: Int = -1,
        @Query("limit") limit: Int = 1
    ): EventResponse
}
