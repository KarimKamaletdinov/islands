package ru.agrogames.islands.common

import okhttp3.Headers
import okhttp3.Request
import java.util.Base64

object ImageKit {
    private const val base = "https://api.imagekit.io/v1"
    private const val key = "private_AnD2X2kXHZIVnwZDYl212LHxPxg="
    val listAll get() = Request.Builder()
        .url("$base/files")
        .headers(Headers.headersOf("Authorization", "Basic " + Base64.getEncoder().encodeToString((key + ":").toByteArray())))
        .build()
}