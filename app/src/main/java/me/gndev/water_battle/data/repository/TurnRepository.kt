package me.gndev.water_battle.data.repository

import me.gndev.water_battle.data.dao.TurnDao
import me.gndev.water_battle.data.entity.Turn
import me.gndev.water_battle.util.DateTimeUtils
import javax.inject.Inject

class TurnRepository @Inject constructor(
    private val turnDao: TurnDao
) {
    suspend fun insertTurn(turn: Turn) {
        turnDao.insert(turn)
    }

    suspend fun getTodayTurns(): List<Turn> {
        return turnDao.getByDate(DateTimeUtils.getTodayAsNumber())
    }
}