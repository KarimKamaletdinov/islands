package com.agrogames.islandsofwar.islands.impl

import android.content.Context
import com.agrogames.islandsofwar.factories.UnitFactory
import com.agrogames.islandsofwar.islands.abs.Island
import com.agrogames.islandsofwar.map.impl.Map
import org.json.JSONObject

object IslandFactory {
    fun parse(context: Context, id: Int, text: String): Island {
        val json = JSONObject(text)
        val map: Map = Map.fromJsonBin("map" + json.getInt("map_id") + ".txt")
        val unitsJson = json.getJSONArray("units")
        val units = (0 until unitsJson.length()).map {
            val unit = unitsJson.getJSONObject(it)
            val result =  if (unit.has("content")) {
                val contentJson = unit.getJSONArray("content")
                val content = (0 until contentJson.length()).map { contentJson.getString(it) }
                UnitFactory.get(
                        unit.getString("name"),
                        unit.getInt("x"),
                        unit.getInt("y"),
                        *content.toTypedArray())
            } else {
                UnitFactory.get(
                        unit.getString("name"),
                        unit.getInt("x"),
                        unit.getInt("y"))
            }
            if (unit.has("health")) {
                result.loseHealth(result.health.start - unit.getInt("health"))
            }
            result;
        }
        val a = json.getJSONObject("ship")
        val contentJson = a.getJSONArray("content")
        val content = (0 until contentJson.length()).map { contentJson.getString(it) }.toTypedArray()
        val attacker = UnitFactory.get(
                a.getString("name"),
                1,
                1,
                *content)
        return Island(id, map, units.toTypedArray(), attacker)
    }
}