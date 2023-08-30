package com.agrogames.islandsofwar.islands.impl

import android.content.Context
import com.agrogames.islandsofwar.islands.abs.Island
import com.agrogames.islandsofwar.islands.abs.IslandProvider
import org.json.JSONException
import java.io.IOException
import java.util.Scanner

class LocalIslandProvider(private val context: Context) : IslandProvider {
    override val my
        get() = getIslands("m")
    override val attackable
        get() = getIslands("e")

    private fun getIslands(prefix: String): Array<Island> {
        try {
            val islandFiles = context.assets.list("islands")
            val islands: MutableList<Island> = ArrayList()
            for (file in islandFiles!!) {
                if (file.startsWith(prefix)) {
                    islands.add(IslandFactory.parse(context, file.substring(1, file.length - 5).toInt(),
                            Scanner(context.assets.open("islands/$file")).useDelimiter("\\A").next()))
                }
            }
            return islands.toTypedArray()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return arrayOf()
    }

    override fun getMyById(id: Int): Island {
        return IslandFactory.parse(context, id,
                Scanner(context.assets.open("islands/m$id.json")).useDelimiter("\\A").next())
    }

    override fun getAttackableById(id: Int): Island {
        return IslandFactory.parse(context, id,
                Scanner(context.assets.open("islands/e$id.json")).useDelimiter("\\A").next())
    }
}