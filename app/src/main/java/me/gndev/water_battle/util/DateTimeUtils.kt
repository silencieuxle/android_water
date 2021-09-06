package me.gndev.water_battle.util

import java.util.*

object DateTimeUtils {
    fun getTodayAsNumber(): Long {
        val datetimeInstance = Calendar.getInstance()
        val year = datetimeInstance.get(Calendar.YEAR)
        val month = datetimeInstance.get(Calendar.MONTH)
        val day = datetimeInstance.get(Calendar.DAY_OF_MONTH)
        return "$year$month$day".toLong()
    }

    fun getCurrentTimeAsNumber(): Long {
        val datetimeInstance = Calendar.getInstance()
        val hour = datetimeInstance.get(Calendar.HOUR_OF_DAY) + 1
        val minute = datetimeInstance.get(Calendar.MINUTE) + 1
        val second = datetimeInstance.get(Calendar.SECOND) + 1
        return "$hour$minute$second".toLong()
    }
}