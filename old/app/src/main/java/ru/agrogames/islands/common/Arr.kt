package ru.agrogames.islands.common

import org.json.JSONArray

object Arr {
    fun indexOf(array: IntArray, item: Int): Int {
        var i = 0
        val arrayLength = array.size
        while (i < arrayLength) {
            val at = array[i]
            if (at == item) return i
            i++
        }
        return -1
    }
}

fun JSONArray.toJSONObjectList() = (0 until this.length()).map { this.getJSONObject(it) }
fun JSONArray.toStringList() = (0 until this.length()).map { this.getString(it) }