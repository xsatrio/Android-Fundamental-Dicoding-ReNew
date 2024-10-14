package com.dicoding.dicodingevent.ui.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.Results
import com.dicoding.dicodingevent.database.entity.EventFinishedEntity
import com.dicoding.dicodingevent.repository.EventRepository

class FinishedViewModel(eventRepository: EventRepository) : ViewModel() {
    val finishedEvents: LiveData<Results<List<EventFinishedEntity>>> =
        eventRepository.getEventFinished()
}