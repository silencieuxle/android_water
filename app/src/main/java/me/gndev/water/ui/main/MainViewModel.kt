package me.gndev.water.ui.main

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.gndev.water.core.model.ViewModelBase
import me.gndev.water.data.entity.Turn
import me.gndev.water.data.repository.GameRepository
import me.gndev.water.data.repository.TurnRepository
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