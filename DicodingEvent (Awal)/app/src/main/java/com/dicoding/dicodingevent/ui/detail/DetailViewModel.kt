package com.dicoding.dicodingevent.ui.detail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.data.ResponseCode
import com.dicoding.dicodingevent.data.response.Event
import com.dicoding.dicodingevent.data.response.EventDetailResponse
import com.dicoding.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.UnknownHostException

class DetailViewModel : ViewModel() {

    private val _detailEvent = MutableLiveData<Event>()
    val detailEvent: LiveData<Event> = _detailEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchDetailEvent(context: Context, id: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailEvent(id)
        client.enqueue(object : Callback<EventDetailResponse> {
            override fun onResponse(call: Call<EventDetailResponse>, response: Response<EventDetailResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val detailEventResponse = response.body()
                    if (detailEventResponse != null) {
                        _detailEvent.value = detailEventResponse.event
                    }
                } else {
                    _errorMessage.value = when (response.code()) {
                        ResponseCode.RC_UNAUTHORIZED.value -> context.getString(R.string.error401)
                        ResponseCode.RC_FORBIDDEN.value -> context.getString(R.string.error403)
                        ResponseCode.RC_NOT_FOUND.value -> context.getString(R.string.error404)
                        else -> "An error occurred: ${response.message()}"
                    }
                }
            }

            override fun onFailure(call: Call<EventDetailResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = when (t) {
                    is UnknownHostException -> context.getString(R.string.title_error)
                    else -> "Network failure: ${t.message}"
                }
            }
        })
    }

}

