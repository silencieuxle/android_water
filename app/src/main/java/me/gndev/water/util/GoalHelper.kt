package me.gndev.water.util

import me.gndev.water.core.constant.UserSex

object GoalHelper {
    fun getRecommendedGoal(age: Int, sex: String): Int {
        when (age) {
            in 9..13 -> {
                return if (sex == UserSex.MALE) (2.4 * 1000).toInt()
                else (2.1 * 1000).toInt()
            }
            in 14..18 -> {
                return if (sex == UserSex.MALE) (3.3 * 1000).toInt()
                else (2.3 * 1000).toInt()
            }
            in 19..130 -> {
                return if (sex == UserSex.MALE) (3.7 * 1000).toInt()
                else (2.7 * 1000).toInt()
            }
            else -> return 2000
        }
    }
}