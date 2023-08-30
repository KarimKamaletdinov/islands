package com.agrogames.islandsofwar.map.impl

import com.agrogames.islandsofwar.common.JsonBin
import com.agrogames.islandsofwar.engine.abs.common.Cell
import com.agrogames.islandsofwar.engine.abs.map.MapObject
import com.agrogames.islandsofwar.map.abs.MapParams
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.util.Dictionary
import java.util.Hashtable

class Map private constructor(override val map: Array<MapObject>) : com.agrogames.islandsofwar.map.abs.Map {

    companion object {
        private val cache: Dictionary<String, String> = Hashtable()

        fun fromJsonBin(name: String): Map {
            if(name !in cache.keys().toList()){
                cache.put(name, JSONObject(OkHttpClient().newCall(JsonBin.maps).execute().body!!.string())
                    .getJSONObject("record").getString(name))
            }
            return load(cache.get(name))
        }

        private fun load(fileContent: String): Map {
            val map: MutableList<MapObject> = ArrayList()
            val rows = fileContent.split("\n".toRegex()).dropLastWhile { it.isEmpty() }
            for (i in rows.indices) {
                val row = rows[i]
                for (j in row.indices) {
                    val symbol = row[j]
                    if (symbol == 'w') {
                        map.add(Water(Cell(j, MapParams.Height - i)))
                    }
                }
            }
            return Map(map.toTypedArray())
        }
    }
}