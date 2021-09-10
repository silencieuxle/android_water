package me.gndev.water.data.share_preferences

import android.content.SharedPreferences

interface PrefManager {
    fun setVal(key: String, value: Long)

    fun getLongVal(key: String, defaultVal: Long = 0L): Long

    fun setVal(key: String, value: Boolean)

    fun getBooleanVal(key: String, defaultVal: Boolean = false): Boolean

    fun setVal(key: String, value: String)

    fun setVal(key: String, value: Int)

    fun getIntVal(key: String, defaultVal: Int = 0): Int

    fun getStringVal(key: String, defaultVal: String = ""): String

    fun contain(key: String): Boolean

    fun remove(key: String)

    fun clearAll()

    fun registerOnChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener)
}