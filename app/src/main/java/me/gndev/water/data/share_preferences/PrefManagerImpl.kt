package me.gndev.water.data.share_preferences

import android.content.SharedPreferences
import javax.inject.Inject

class PrefManagerImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : PrefManager {

    override fun setVal(key: String, value: Long) {
        sharedPreferences.edit().putLong(key, value).apply()
    }

    override fun getLongVal(key: String, defaultVal: Long): Long {
        return sharedPreferences.getLong(key, 0)
    }

    override fun setVal(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    override fun getBooleanVal(key: String, defaultVal: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultVal)
    }

    override fun setVal(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun getStringVal(key: String, defaultVal: String): String? {
        return sharedPreferences.getString(key, "defaultVal")
    }

    override fun setVal(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    override fun getIntVal(key: String, defaultVal: Int): Int {
        return sharedPreferences.getInt(key, defaultVal)
    }

    override fun contain(key: String): Boolean {
        return sharedPreferences.contains(key)
    }

    override fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    override fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }
}