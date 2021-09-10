package me.gndev.water.core.base

import androidx.lifecycle.ViewModel
import me.gndev.water.core.event.ErrorLiveEvent
import me.gndev.water.core.event.LiveEventBase
import me.gndev.water.core.event.NavigationLiveEvent

abstract class ViewModelBase : ViewModel() {
    val navLiveEvent = NavigationLiveEvent()
    val errorLiveEvent = ErrorLiveEvent()
    val loadingEvent = LiveEventBase<Boolean>()
}