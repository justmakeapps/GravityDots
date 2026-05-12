package com.justmakeapps.gravitydots.data

import android.content.Context
import android.content.SharedPreferences

class GamePrefs(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("gravity_dots_prefs", Context.MODE_PRIVATE)

    var highestUnlockedLevel: Int
        get() = prefs.getInt("highest_unlocked", 1)
        set(v) = prefs.edit().putInt("highest_unlocked", v).apply()

    var bonusWalls: Int
        get() = prefs.getInt("bonus_walls", 0)
        set(v) = prefs.edit().putInt("bonus_walls", v).apply()

    var adsRemoved: Boolean
        get() = prefs.getBoolean("ads_removed", false)
        set(v) = prefs.edit().putBoolean("ads_removed", v).apply()

    fun getStars(level: Int): Int = prefs.getInt("stars_$level", 0)

    fun setStars(level: Int, stars: Int) {
        val current = getStars(level)
        if (stars > current) prefs.edit().putInt("stars_$level", stars).apply()
    }

    fun unlockLevel(level: Int) {
        if (level > highestUnlockedLevel) highestUnlockedLevel = level
    }

    fun addBonusWalls(count: Int) {
        bonusWalls += count
    }

    fun consumeBonusWall(): Boolean {
        return if (bonusWalls > 0) {
            bonusWalls--
            true
        } else false
    }
}
