package com.agrogames.islandsofwar.common

import android.content.SharedPreferences

var SharedPreferences.sound
    get() = this.getBoolean("sound", true)
    set(value) = this.edit().putBoolean("sound", value).apply()