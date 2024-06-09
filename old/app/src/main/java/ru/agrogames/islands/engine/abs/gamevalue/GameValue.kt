package ru.agrogames.islands.engine.abs.gamevalue

open class GameValue<T>(val start: T) {
    var current: T

    init {
        current = start
    }
}