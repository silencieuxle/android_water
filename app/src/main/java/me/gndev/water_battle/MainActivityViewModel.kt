package me.gndev.water_battle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.gndev.water_battle.core.model.DataResult
import me.gndev.water_battle.core.model.ViewModelBase
import me.gndev.water_battle.data.entity.Game
import me.gndev.water_battle.data.repository.GameRepository
import me.gndev.water_battle.util.DateTimeUtils
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModelBase() {
    private val _currentGame: MutableLiveData<Game> = MutableLiveData()
    val currentGame: LiveData<Game> get() = _currentGame

    fun initGame() {
        viewModelScope.launch {
            when (val gameExists = gameRepository.exists(DateTimeUtils.getTodayAsNumber())) {
                is DataResult.Success -> {
                    val result = if (gameExists.data) {
                        gameRepository.getToday()
                    } else {
                        gameRepository.insert(Game())
                        gameRepository.getToday()
                    }
                    when (result) {
                        is DataResult.Success -> _currentGame.value = result.data
                        else -> errorLiveEvent.value = result as DataResult.Error
                    }
                }
            }
        }
    }

    fun updateCurrentGame(totalVolume: Int) {
        viewModelScope.launch {
            _currentGame.value?.score = totalVolume
            gameRepository.update(_currentGame.value!!)
        }
    }
}