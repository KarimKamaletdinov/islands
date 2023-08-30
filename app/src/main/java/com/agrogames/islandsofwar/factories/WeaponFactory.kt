package com.agrogames.islandsofwar.factories

import com.agrogames.islandsofwar.engine.abs.common.Point
import com.agrogames.islandsofwar.engine.abs.weapon.IWeapon
import com.agrogames.islandsofwar.engine.impl.weapon.Weapon
import org.json.JSONArray
import org.json.JSONObject

object WeaponFactory {
    val weapons: MutableList<JSONObject> = ArrayList()
    private fun getJSONWeapon(name: String): JSONObject {
        return weapons.find { it.getString("name") == name } !!
    }

    private fun getWeapon(name: String, location: Point): IWeapon {
        return weapon(getJSONWeapon(name), location)
    }

    fun weapons(json: JSONArray): Array<IWeapon> {
        return( 0 until json.length()).map {
            when (val o = json[it]) {
                is String -> {
                    getWeapon(o, Point(0f, 0f))
                }

                is JSONObject -> {
                    getWeapon(o.getString("name"), PointFactory.point(o.getJSONObject("location")))
                }

                else -> {
                    throw Exception("O must be String or JsonObject, but it was " + o::class.java.typeName)
                }
            }
        }.toTypedArray()
    }

    private fun weapon(json: JSONObject, location: Point): IWeapon {
        val bullet = json.getJSONObject("bullet")
        return Weapon(
                location, json.getDouble("reload").toFloat(),
                "weapons/" + if (json.has("texture")) json.getString("texture") else json.getString("name"), json.getDouble("range").toFloat(), json.getDouble("rotation_speed").toFloat(),
                PointFactory.points(json.getJSONArray("bullet_starts")),
                "bullets/" + if (bullet.has("texture")) bullet.getString("texture") else bullet.getString("name"),
                bullet.getInt("damage"), bullet.getDouble("speed").toFloat(),
                bullet.getInt("flight_height"),
                bullet.getInt("target_height"),
                bullet.getBoolean("bang"),
                if (bullet.getBoolean("bang")) bullet.getDouble("bang_range").toFloat() else 0f,
                bullet.getString("creation_sound"))
    }
}