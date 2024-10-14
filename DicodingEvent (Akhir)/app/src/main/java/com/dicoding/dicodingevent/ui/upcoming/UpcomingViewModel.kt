package com.dicoding.dicodingevent.ui.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.Results
import com.dicoding.dicodingevent.database.entity.EventActiveEntity
import com.dicoding.dicodingevent.repository.EventRepository

class UpcomingViewModel(eventRepository: EventRepository) : ViewModel() {

    val activeEvents: LiveData<Results<List<EventActiveEntity>>> = eventRepository.getEventActive()
}
