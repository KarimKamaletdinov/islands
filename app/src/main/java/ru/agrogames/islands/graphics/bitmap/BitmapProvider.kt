package ru.agrogames.islands.graphics.bitmap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLUtils
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import ru.agrogames.islands.common.ImageKit
import ru.agrogames.islands.common.mProgram

object BitmapProvider {
    private var cachedBitmaps: List<Pair<String, Bitmap>>? = null
    private lateinit var cachedTextures: Map<String, BitmapDescriptor>

    fun initCache(context: Context) {
        val textures = (loadFolder(context, "textures") + loadImageKitFolder()).toTypedArray()
        cachedBitmaps = textures.indices.map {
            Pair(
                textures[it],
                if (textures[it].startsWith("imagekit/"))
                    loadImageKitFile(textures[it].substring(9))
                else
                    loadFile(context, textures[it].substring(9))
            )
        }
    }

    fun initProgram() {
        mProgram = GLES20.glCreateProgram()
        GLES20.glUseProgram(mProgram)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        val textureUnit = IntArray(cachedBitmaps!!.size)
        GLES20.glGenTextures(textureUnit.size, textureUnit, 0)
        cachedTextures = mapOf(*cachedBitmaps!!.indices.map {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureUnit[it])
            val bitmap = cachedBitmaps!![it].second
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
            GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)
            val descriptor = BitmapDescriptor()
            descriptor.id = textureUnit[it]
            descriptor.width = bitmap.width
            descriptor.height = bitmap.height
            Pair(if (cachedBitmaps!![it].first.startsWith("imagekit/"))
                    "imagekit/" + cachedBitmaps!![it].first.split(
                    '/').last()
                else cachedBitmaps!![it].first.substring(9),
                descriptor)
        }.toTypedArray())
        cachedBitmaps = null
    }

    fun load(name: String): BitmapDescriptor {
        return cachedTextures["$name.png"] ?: throw Exception("No such image: $name")
    }

    private fun loadFolder(context: Context, name: String): List<String> {
        return context.assets.list(name)!!.flatMap {
            if (it.contains(".")) {
                listOf("$name/$it")
            } else {
                loadFolder(context, "$name/$it")
            }
        }
    }

    private fun loadImageKitFolder(): List<String> {
        val json = JSONArray(OkHttpClient().newCall(ImageKit.listAll).execute().body!!.string())
        return (0 until json.length()).map { "imagekit/" + json.getJSONObject(it).getString("url") }
    }

    private fun loadFile(context: Context, name: String): Bitmap {
        Log.e("IOW", name)
        return BitmapFactory.decodeStream(context.assets.open("textures/$name"))
    }

    private fun loadImageKitFile(name: String): Bitmap {
        return BitmapFactory.decodeStream(
            OkHttpClient()
                .newCall(Request.Builder().url(name).build())
                .execute().body!!.byteStream()
        )
    }
}