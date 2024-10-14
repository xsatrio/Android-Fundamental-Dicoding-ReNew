package com.dicoding.dicodingevent.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingevent.SettingPreferences
import com.dicoding.dicodingevent.data.Results
import com.dicoding.dicodingevent.database.entity.EventActiveEntity
import com.dicoding.dicodingevent.database.entity.EventFinishedEntity
import com.dicoding.dicodingevent.repository.EventRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    eventRepository: EventRepository,
    private val settingPreferences: SettingPreferences
) : ViewModel() {
    val activeEvents: LiveData<Results<List<EventActiveEntity>>> = eventRepository.getEventActive()
    val finishedEvents: LiveData<Results<List<EventFinishedEntity>>> =
        eventRepository.getEventFinished()

    fun getThemeSettings(): LiveData<Boolean> = settingPreferences.getThemeSetting().asLiveData()

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            settingPreferences.saveThemeSetting(isDarkModeActive)
        }
    }

    fun getReminderSettings(): LiveData<Boolean> =
        settingPreferences.getReminderSetting().asLiveData()

    fun saveReminderSetting(isNotifyActive: Boolean) {
        viewModelScope.launch {
            settingPreferences.saveReminderSetting(isNotifyActive)
        }
    }
}
