package com.dicoding.dicodingevent.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingevent.data.Results
import com.dicoding.dicodingevent.database.entity.EventDetailEntity
import com.dicoding.dicodingevent.repository.EventRepository
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: EventRepository
) : ViewModel() {

    private val _eventId = MutableLiveData<Int>()
    val eventDetail: LiveData<Results<EventDetailEntity>> = _eventId.switchMap { eventId ->
        repository.getEventDetail(eventId)
    }

    fun setEventId(eventId: Int) {
        if (_eventId.value != eventId) {
            _eventId.value = eventId
        }
    }

    fun toggleFavorite(event: EventDetailEntity, favoriteState: Boolean) {
        viewModelScope.launch {
            repository.setEventFavorite(event, favoriteState)
        }
    }
}
