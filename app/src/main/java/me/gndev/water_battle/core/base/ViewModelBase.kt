package me.gndev.water_battle.core.base

import androidx.lifecycle.ViewModel
import me.gndev.water_battle.core.event.ErrorLiveEvent
import me.gndev.water_battle.core.event.LiveEventBase
import me.gndev.water_battle.core.event.NavigationLiveEvent

abstract class ViewModelBase : ViewModel() {
    val navLiveEvent = NavigationLiveEvent()
    val errorLiveEvent = ErrorLiveEvent()
    val loadingEvent = LiveEventBase<Boolean>()
}