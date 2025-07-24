package com.example.hustletrack

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TimerViewModel(application: Application) : AndroidViewModel(application) {

    private var timerJob: Job? = null
    private var startTime = 0L
    private var elapsedMillis = 0L

    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime: StateFlow<Long> = _elapsedTime

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    var showHistory by mutableStateOf(false)

    // ✅ Room database access
    private val db = AppDatabase.getDatabase(application)
    private val timeEntryDao = db.timeEntryDao()

    // ✅ Expose all entries to UI
    val allEntries: StateFlow<List<TimeEntry>> =
        timeEntryDao.getAll().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // ✅ Start timer
    fun startTimer() {
        if (_isRunning.value) return

        _isRunning.value = true
        startTime = System.currentTimeMillis() - elapsedMillis

        timerJob = viewModelScope.launch {
            while (true) {
                elapsedMillis = System.currentTimeMillis() - startTime
                _elapsedTime.value = elapsedMillis
                delay(100L)
            }
        }
    }

    // ✅ Stop timer
    fun stopTimer() {
        _isRunning.value = false
        timerJob?.cancel()
    }

    // ✅ Reset timer
    fun resetTimer() {
        stopTimer()
        elapsedMillis = 0L
        _elapsedTime.value = 0L
    }

    // ✅ Delete a time entry
    fun deleteEntry(entry: TimeEntry) {
        viewModelScope.launch {
            timeEntryDao.delete(entry)
        }
    }

    // ✅ Save a time entry
    fun saveEntry(startTime: Long, endTime: Long) {
        val duration = endTime - startTime
        viewModelScope.launch {
            val entry = TimeEntry(0, startTime, endTime, duration)
            timeEntryDao.insert(entry)
        }
    }
    fun stopAndSave() {
        _isRunning.value = false
        // TODO: Save logic here
        // e.g., save elapsed time to a database or file
    }
}

