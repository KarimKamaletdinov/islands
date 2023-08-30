package com.agrogames.islandsofwar.engine.abs.gamevalue

open class GameValue<T>(val start: T) {
    var current: T

    init {
        current = start
    }
}