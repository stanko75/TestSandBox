package com.example.testsandbox

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class TickEventBus {
    private val _tickEvent = MutableLiveData<Unit>()
    val tickEvent: LiveData<Unit> get() = _tickEvent

    fun sendTick() {
        _tickEvent.postValue(Unit)
    }
}