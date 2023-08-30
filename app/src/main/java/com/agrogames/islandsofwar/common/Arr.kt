package com.agrogames.islandsofwar.common

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