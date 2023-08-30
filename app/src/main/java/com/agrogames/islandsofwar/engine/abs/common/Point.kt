package com.agrogames.islandsofwar.engine.abs.common

import java.util.Objects

class Point {
    var x: Float
    var y: Float

    constructor(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    constructor(cell: Cell?) {
        x = cell!!.x - 0.5f
        y = cell.y - 0.5f
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val point = o as Point
        return java.lang.Float.compare(point.x, x) == 0 && java.lang.Float.compare(point.y, y) == 0
    }

    override fun hashCode(): Int {
        return Objects.hash(x, y)
    }

    fun rotate(angle: Float): Point {
        return Point((x.toDouble() * Math.cos(angle.toDouble()) - y.toDouble() * Math.sin(angle.toDouble())).toFloat(), (x.toDouble() * Math.sin(angle.toDouble()) + y.toDouble() * Math.cos(angle.toDouble())).toFloat())
    }

    override fun toString(): String {
        return "{$x;$y}"
    }
}