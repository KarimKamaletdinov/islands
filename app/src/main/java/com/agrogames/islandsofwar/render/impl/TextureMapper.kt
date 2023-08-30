package com.agrogames.islandsofwar.render.impl

object TextureMapper {
    fun join(texture: String): String {
        return texture
    }

    fun join(texture: String, state: String): String {
        return "$texture/$state"
    }
}