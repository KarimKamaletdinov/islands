package ru.agrogames.islands.common

import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

object JsonBin {
    private const val base = "https://api.jsonbin.io/v3/"
    private const val accessKey = "\$2b\$10\$TZnboXDZOf0HRziDRwJkve8eD2Kk/dLiSsVfSV3ZkGB7ia1eYLxzy"
    private const val islandsBin = "64eca515b89b1e2299d70d6e"
    private const val mapsBin = "64ecd125b89b1e2299d71e94"

    private fun bin(id: String) = Request.Builder()
        .url(base + "b/" + id)
        .headers(Headers.headersOf("X-Access-Key", accessKey))
        .build()

    private fun binPost(id: String, body: String) = Request.Builder()
        .url(base + "b/" + id)
        .put(body.toRequestBody("application/json".toMediaTypeOrNull()))
        .headers(Headers.headersOf("X-Access-Key", accessKey))
        .build()
    val islands get() = bin(islandsBin)
    val maps get() = bin(mapsBin)

    fun resetIslands(body: String) = binPost(islandsBin, body)
}