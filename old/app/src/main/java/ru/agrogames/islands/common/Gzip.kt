package ru.agrogames.islands.common


import android.util.Base64
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets.UTF_8
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

val String.gzip: String get() {
    val bos = ByteArrayOutputStream()
    GZIPOutputStream(bos).bufferedWriter(UTF_8).use { it.write(this) }
    return Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT)
}

val String.ungzip: String get() =
    GZIPInputStream(Base64.decode(this, Base64.DEFAULT).inputStream()).bufferedReader(UTF_8).use { it.readText() }
