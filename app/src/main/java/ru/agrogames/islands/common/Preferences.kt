package ru.agrogames.islands.common

import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject
import ru.agrogames.islands.map.MapParams

var SharedPreferences.sound
    get() = this.getBoolean("sound", true)
    set(value) = this.edit().putBoolean("sound", value).apply()
var SharedPreferences.island: List<Array<String?>>
    get(){
        val json = JSONArray(this.getString("island", "[]")).toJSONObjectList()
        return (0 until MapParams.width).map { x -> (0 until MapParams.height).map<Int, String?>
        { y -> json.firstOrNull { it.getInt("x") ==  x && it.getInt("y") == y}?.getString("name") }.toTypedArray() }
    }
    set(value) = this.edit().putString("island", JSONArray(value.withIndex()
        .flatMap { (x, data) ->  data.withIndex().map { (y, v) -> Triple(x, y, v) } }
        .filter { it.third != null }.map { JSONObject().put("x", it.first).put("y", it.second).put("name", it.third!!) })
        .toString()).apply()

var SharedPreferences.units: MutableMap<String, Int>?
    get() {
        val json = JSONObject(this.getString("units", null) ?: return null)
        val result = mutableMapOf<String, Int>()
        for(key in json.keys()){
            result[key] = json.getInt(key)
        }
        return result
    }
    set(value) = this.edit().putString("units", JSONObject(value!!.toMap()).toString()).apply()

var SharedPreferences.cheats: Boolean
    get() = this.getBoolean("cheats", false)
    set(value) = this.edit().putBoolean("cheats", value).apply()