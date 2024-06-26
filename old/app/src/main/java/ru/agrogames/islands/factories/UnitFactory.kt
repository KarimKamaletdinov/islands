package ru.agrogames.islands.factories

import org.json.JSONException
import org.json.JSONObject
import ru.agrogames.islands.engine.abs.common.Cell
import ru.agrogames.islands.engine.abs.transport.TransportUnit
import ru.agrogames.islands.engine.abs.unit.IUnit
import ru.agrogames.islands.engine.impl.unit.BigShip
import ru.agrogames.islands.engine.impl.unit.Building
import ru.agrogames.islands.engine.impl.unit.LandUnit
import ru.agrogames.islands.engine.impl.unit.Plane
import ru.agrogames.islands.engine.impl.unit.SmallShip

object UnitFactory {
    val units: MutableList<JSONObject> = ArrayList()
    operator fun get(name: String, x: Int, y: Int, vararg contentUnits: String): IUnit {
        val content = contentUnits.map { getJSONUnit(it) }
        return unit(getJSONUnit(name), Cell(x, y), content)
    }

    private fun unit(json: JSONObject, location: Cell, content: List<JSONObject>): IUnit {
        return when (json.getString("type")) {
            "land" -> LandUnit(
                    "units/" + if (json.has("texture")) json.getString("texture") else json.getString("name"),
                    location,
                    WeaponFactory.weapons(json.getJSONArray("weapons")),
                    json.getInt("health"), json.getDouble("speed").toFloat(), json.getDouble("rotation_speed").toFloat())

            "building" -> Building(
                    "units/" + if (json.has("texture")) json.getString("texture") else json.getString("name"),
                    location,
                    WeaponFactory.weapons(json.getJSONArray("weapons")),
                    json.getInt("health")
            )

            "big_ship" -> BigShip(
                    "units/" + if (json.has("texture")) json.getString("texture") else json.getString("name"),
                    location,
                    WeaponFactory.weapons(json.getJSONArray("weapons")),
                    json.getInt("health"), json.getDouble("speed").toFloat(), json.getDouble("rotation_speed").toFloat(),
                    transportUnits(content),
                    json.getInt("min_damage")
            )

            "small_ship" -> SmallShip(
                    "units/" + if (json.has("texture")) json.getString("texture") else json.getString("name"),
                    location,
                    WeaponFactory.weapons(json.getJSONArray("weapons")),
                    json.getInt("health"), json.getDouble("speed").toFloat(), json.getDouble("rotation_speed").toFloat())

            "plane" -> {
                val bomb = json.getJSONObject("bomb")
                Plane(
                        "units/" + if (json.has("texture")) json.getString("texture") else json.getString("name"),
                        json.getInt("health"), json.getDouble("speed").toFloat(), json.getDouble("rotation_speed").toFloat(),
                        json.getInt("bomb_count"),
                        "bullets/" + bomb.getString("texture"),
                        bomb.getInt("power"),
                        bomb.getInt("range").toFloat())
            }

            else -> throw JSONException("Unknown type of Unit: " + json.getString("type"))
        }
    }

    private fun getJSONUnit(name: String) =
            units.find { it.getString("name") == name }!!

    private fun transportUnits(json: List<JSONObject>) =
            json.indices.map { transportUnit(json[it]) }.toTypedArray()

    private fun transportUnit(json: JSONObject) =
            TransportUnit { unit(json, it, listOf()) }
}