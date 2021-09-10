package me.gndev.water_battle.ui.main

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.gndev.water_battle.core.base.ViewModelBase
import me.gndev.water_battle.data.entity.Turn
import me.gndev.water_battle.data.repository.TurnRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val turnRepository: TurnRepository
) : ViewModelBase() {
    fun saveTurn(turn: Turn) {
        viewModelScope.launch {
            turnRepository.insertTurn(turn)
        }
    }
}