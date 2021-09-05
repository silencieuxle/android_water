package me.gndev.water.data.repository

import me.gndev.water.data.dao.TurnDao
import me.gndev.water.data.entity.Turn
import me.gndev.water.util.DateTimeUtils
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