package ru.agrogames.islands.render

object TextureMapper {
    fun join(texture: String): String {
        return texture
    }

    fun join(texture: String, state: String): String {
        return "$texture/$state"
    }
}