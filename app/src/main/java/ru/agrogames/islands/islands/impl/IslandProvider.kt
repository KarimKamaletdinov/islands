package ru.agrogames.islands.islands.impl

import android.content.Context
import androidx.preference.PreferenceManager
import org.json.JSONArray
import org.json.JSONObject
import ru.agrogames.islands.common.map
import ru.agrogames.islands.common.units
import ru.agrogames.islands.engine.abs.common.Cell
import ru.agrogames.islands.engine.abs.transport.TransportUnit
import ru.agrogames.islands.engine.impl.unit.BigShip
import ru.agrogames.islands.islands.abs.Island
import ru.agrogames.islands.islands.abs.IslandProvider
import java.util.Scanner

class IslandProvider(private val context: Context) :
    IslandProvider {
    override val my
        get() = emptyArray<Island>()
    override val attackable
        get() = getIslands()
    private fun getIslands(): Array<Island> {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val response = preferences.getString("islands",
            Scanner(context.assets.open("islands.json")).useDelimiter("\\A").next())!!
        val json = JSONArray(response)
        return (0 until json.length()).map {
            IslandFactory.parse(
                context,
                it + 1,
                json[it].toString()
            )
        }.reversed().toTypedArray()
    }

    override fun getMyById(id: Int): Island {
        throw Exception("NotImplemented")
    }

    override fun getAttackableById(id: Int): Island {
        return getIslands().first { it.id == id }
    }

    fun saveIsland(){
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val response = preferences.getString("islands",
            Scanner(context.assets.open("islands.json")).useDelimiter("\\A").next())!!
        val json = JSONArray(response)
        json.put(JSONObject("""{
            "map": "${preferences.map.toString().replace("\n", "\\n")}",
            "ship": {
              "name": "transport_ship",
              "content": ${preferences.units!!.toList().flatMap { (0 until it.second).map { _ -> it.first } }}
            },
            "units": ${preferences.getString("island", "[]")}
        }""".trimIndent()))
        preferences.edit().putString("islands", json.toString()).apply()
    }

    fun saveIsland(jsonString: String){
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val response = preferences.getString("islands",
            Scanner(context.assets.open("islands.json")).useDelimiter("\\A").next())!!
        val json = JSONArray(response)
        json.put(JSONObject(jsonString))
        preferences.edit().putString("islands", json.toString()).apply()
    }
    fun islandToString(islandId: Int): String{
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val response = preferences.getString("islands",
            Scanner(context.assets.open("islands.json")).useDelimiter("\\A").next())!!
        val json = JSONArray(response)
        return json.getJSONObject(islandId - 1).toString(0).replace("\n", "")
    }
    fun deleteIsland(islandId: Int){
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val response = preferences.getString("islands",
            Scanner(context.assets.open("islands.json")).useDelimiter("\\A").next())!!
        val json = JSONArray(response)
        json.remove(islandId - 1)
        preferences.edit().putString("islands", json.toString()).apply()
    }
}