package me.gndev.water.data.repository

import me.gndev.water.core.model.DataResult
import me.gndev.water.data.dao.GameDao
import me.gndev.water.data.entity.Game
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(
    private val gameDao: GameDao
) {
    suspend fun getAll(): DataResult<List<Game>> {
        return try {
            return DataResult.Success(gameDao.getAll())
        } catch (ex: Exception) {
            DataResult.Error(ex.message)
        }
    }

    suspend fun exists(date: Long): DataResult<Boolean> {
        return try {
            return DataResult.Success(gameDao.exists(date))
        } catch (ex: Exception) {
            DataResult.Error(ex.message)
        }
    }

    suspend fun insert(game: Game): DataResult<Unit> {
        return try {
            gameDao.insert(game)
            DataResult.SuccessNoData
        } catch (ex: Exception) {
            DataResult.Error(ex.message)
        }
    }

    suspend fun getToday(): DataResult<Game?> {
        return try {
            DataResult.Success(gameDao.getToday())
        } catch (ex: Exception) {
            DataResult.Error(ex.message)
        }
    }

    suspend fun update(game: Game): DataResult<Unit> {
        return try {
            gameDao.update(game)
            DataResult.SuccessNoData
        } catch (ex: Exception) {
            DataResult.Error(ex.message)
        }
    }
}