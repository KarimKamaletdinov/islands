package ru.agrogames.islands.common

import ru.agrogames.islands.engine.abs.common.Point

object M {
    fun module(a: Int): Int {
        return if (a >= 0) a else -a
    }

    fun module(a: Float): Float {
        return if (a >= 0) a else -a
    }

    fun module(a: Double): Double {
        return if (a >= 0) a else -a
    }

    fun nearlyEquals(a: Float, b: Float): Boolean {
        return module(a - b) < 0.0001f
    }

    fun sqrt(a: Float): Float {
        return Math.sqrt(a.toDouble()).toFloat()
    }

    fun dist(a: Point, b: Point): Float {
        val w = module(a.x - b.x)
        val h = module(a.y) - b.y
        return sqrt(w * w + h * h)
    }

    fun middle(a: Point, b: Point): Point {
        return Point((a.x + b.x) / 2f, (a.y + b.y) / 2f)
    }
}