package com.dicoding.dicodingevent.ui.finished

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.data.ResponseCode
import com.dicoding.dicodingevent.data.response.EventResponse
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.UnknownHostException

class FinishedViewModel : ViewModel() {

    private val _events = MutableLiveData<List<ListEventsItem>>()
    val events: LiveData<List<ListEventsItem>> = _events

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: MutableLiveData<String?> = _errorMessage

    fun getEvents(context: Context) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().listEvent("0")
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _events.value = response.body()?.listEvents ?: listOf()
                } else {
                    _errorMessage.value = when (response.code()) {
                        ResponseCode.RC_UNAUTHORIZED.value -> context.getString(R.string.error401)
                        ResponseCode.RC_FORBIDDEN.value -> context.getString(R.string.error403)
                        ResponseCode.RC_NOT_FOUND.value -> context.getString(R.string.error404)
                        else -> "An error occurred: ${response.message()}"
                    }
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = when (t) {
                    is UnknownHostException -> context.getString(R.string.title_error)
                    else -> "Network failure: ${t.message}"
                }
            }
        })
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}