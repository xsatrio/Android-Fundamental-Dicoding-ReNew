package com.dicoding.dicodingevent.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.response.EventResponse
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.UnknownHostException

class SearchViewModel : ViewModel() {

    private val _events = MutableLiveData<List<ListEventsItem>>()
    val events: LiveData<List<ListEventsItem>> = _events

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: MutableLiveData<String?> = _errorMessage

    init {
        getEvents("")
    }

    fun getEvents(keyword: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchEvent(-1, keyword)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _events.value = response.body()?.listEvents?: listOf()
                } else {
                    _errorMessage.value = when (response.code()) {
                        401 -> "401 Unauthorized: Bad Request"
                        403 -> "403 Forbidden: Access Denied"
                        404 -> "404 Not Found: Resource not found"
                        else -> "An error occurred: ${response.message()}"
                    }
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = when (t) {
                    is UnknownHostException -> "Periksa Jaringan Anda"
                    else -> "Network failure: ${t.message}"
                }
            }
        })
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}