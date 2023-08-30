package com.agrogames.islandsofwar.engine.abs.common

import java.util.Objects

class Cell {
    val x: Int
    val y: Int

    constructor(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    constructor(point: Point?) {
        x = (point!!.x + 0.5f).toInt()
        y = (point.y + 0.5f).toInt()
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val cell = o as Cell
        return x == cell.x && y == cell.y
    }

    fun nearlyEquals(p: Point): Boolean {
        return p.x + 0.7f >= x && p.x + 0.5f <= x && p.y + 0.7f >= y && p.y + 0.5f <= y
    }

    override fun toString(): String {
        return "{$x;$y}"
    }

    override fun hashCode(): Int {
        return Objects.hash(x, y)
    }
}