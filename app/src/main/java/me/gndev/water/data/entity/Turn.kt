package me.gndev.water.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import me.gndev.water.core.constant.Weapon
import me.gndev.water.util.DateTimeUtils

@Entity(tableName = "turns")
data class Turn(
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
    var time: Long = DateTimeUtils.getCurrentTimeAsNumber(),
    var date: Long = DateTimeUtils.getTodayAsNumber(),
    var volume: Int,
    var weapon: Int = Weapon.CUP,
    var gameId: Int
)