package com.agrogames.islandsofwar.islands.impl

import android.content.Context
import com.agrogames.islandsofwar.common.JsonBin
import com.agrogames.islandsofwar.islands.abs.Island
import com.agrogames.islandsofwar.islands.abs.IslandProvider
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject

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
            val client = OkHttpClient()
            val response = client.newCall(JsonBin.islands).execute().body!!.string()
            jsonCache = JSONObject(response).getJSONArray("record")
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
}