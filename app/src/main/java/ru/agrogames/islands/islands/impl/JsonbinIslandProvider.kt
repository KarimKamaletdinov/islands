package ru.agrogames.islands.islands.impl

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject
import ru.agrogames.islands.common.JsonBin
import ru.agrogames.islands.common.map
import ru.agrogames.islands.common.units
import ru.agrogames.islands.islands.abs.Island
import ru.agrogames.islands.islands.abs.IslandProvider
import java.util.Scanner

class JsonbinIslandProvider(private val context: Context) : IslandProvider {
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
        }.toTypedArray()
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
        json.put(0, JSONObject("""{
            "map": "${preferences.map.toString().replace("\n", "\\n")}",
            "ship": {
              "name": "transport_ship",
              "content": ${preferences.units!!.toList().flatMap { (0 until it.second).map { _ -> it.first } }}
            },
            "units": ${preferences.getString("island", "[]")}
        }""".trimIndent()))
        preferences.edit().putString("islands", json.toString()).apply()
    }
}