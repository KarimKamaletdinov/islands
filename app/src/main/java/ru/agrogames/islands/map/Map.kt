package ru.agrogames.islands.map

import okhttp3.OkHttpClient
import org.json.JSONObject
import ru.agrogames.islands.common.JsonBin
import ru.agrogames.islands.engine.abs.common.Cell
import ru.agrogames.islands.engine.abs.map.MapObject

class Map private constructor(val name: String, val map: Array<MapObject>) {

    companion object {
        private lateinit var cache: JSONObject

        fun init() {
            cache = JSONObject(OkHttpClient().newCall(JsonBin.maps).execute().body!!.string()).getJSONObject("record")
        }
        fun fromJsonBin(name: String): Map {
            return load(name, cache.getString(name))
        }

        fun load(name: String, fileContent: String): Map {
            val map: MutableList<MapObject> = ArrayList()
            val rows = fileContent.split("\n".toRegex()).dropLastWhile { it.isEmpty() }
            for (i in rows.indices) {
                val row = rows[i]
                for (j in row.indices) {
                    val symbol = row[j]
                    if (symbol == 'w') {
                        map.add(Water(Cell(j, MapParams.height - i)))
                    }
                }
            }
            return Map(name, map.toTypedArray())
        }
    }
}