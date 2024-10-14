package com.dicoding.dicodingevent.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.Results
import com.dicoding.dicodingevent.database.entity.EventDetailEntity
import com.dicoding.dicodingevent.repository.EventRepository

class FavoriteViewModel(eventRepository: EventRepository) : ViewModel() {
    val favoritedEvents: LiveData<Results<List<EventDetailEntity>>> =
        eventRepository.getEventFavorited()
}
