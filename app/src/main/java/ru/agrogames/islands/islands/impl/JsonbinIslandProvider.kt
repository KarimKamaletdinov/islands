package ru.agrogames.islands.islands.impl

import android.content.Context
import androidx.preference.PreferenceManager
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject
import ru.agrogames.islands.common.JsonBin
import ru.agrogames.islands.common.units
import ru.agrogames.islands.islands.abs.Island
import ru.agrogames.islands.islands.abs.IslandProvider
import java.util.Scanner

class JsonbinIslandProvider(private val context: Context) : IslandProvider {
    override val my
        get() = emptyArray<Island>()
    override val attackable
        get() = getIslands()
    companion object {
        private var jsonCache: JSONArray? = null
    }
    private fun getIslands(): Array<Island> {
        if(jsonCache == null) {
            val response = Scanner(context.assets.open("islands.json")).useDelimiter("\\A").next()
            jsonCache = JSONArray(response)
        }
        return (0 until jsonCache!!.length()).map {
            IslandFactory.parse(
                context,
                it + 1,
                jsonCache!![it].toString()
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
        if(jsonCache == null) {
            val client = OkHttpClient()
            val response = client.newCall(JsonBin.islands).execute().body!!.string()
            jsonCache = JSONObject(response).getJSONArray("record")
        }
        jsonCache!!.put(JSONObject("""{
            "map_id": 1,
            "ship": {
              "name": "transport_ship",
              "content": ${preferences.units!!.toList().flatMap { (0 until it.second).map { _ -> it.first } }}
            },
            "units": ${preferences.getString("island", "[]")}
        }""".trimIndent()))
        OkHttpClient().newCall(JsonBin.resetIslands(jsonCache.toString())).execute().body!!.string()
    }
}