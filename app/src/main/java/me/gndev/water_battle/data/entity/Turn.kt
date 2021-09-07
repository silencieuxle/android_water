package me.gndev.water_battle.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import me.gndev.water_battle.core.constant.Weapon
import me.gndev.water_battle.util.DateTimeUtils

@Entity(tableName = "turns")
data class Turn(
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
    var time: Long = DateTimeUtils.getCurrentTimeAsNumber(),
    var date: Long = DateTimeUtils.getTodayAsNumber(),
    var volume: Int,
    var weapon: String = Weapon.CUP,
    var gameId: Int
)