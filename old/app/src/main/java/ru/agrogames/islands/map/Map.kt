package ru.agrogames.islands.map

import org.json.JSONObject
import ru.agrogames.islands.engine.abs.common.Cell
import ru.agrogames.islands.engine.abs.map.MapObject

class Map (val name: String, val mp: Array<MapObject>) {

    override fun toString(): String {
        var result = ""
        for (y in MapParams.height downTo 1) {
            for (x in 0..<MapParams.width) {
                result += if(mp.any { it.territory[0] == Cell(x, y) }) {
                    "w"
                } else {
                    " "
                }
            }
            result += "\n"
        }
        return result
    }
    companion object {        fun load(name: String, fileContent: String): Map {
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