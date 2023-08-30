package com.agrogames.islandsofwar.sounds

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.util.Log
import androidx.preference.PreferenceManager
import com.agrogames.islandsofwar.common.sound
import java.io.IOException

object SoundPlayer {
    var enabled = false
    private lateinit var soundPool: SoundPool
    private lateinit var audioManager: AudioManager
    private val sounds: MutableMap<String, Int> = HashMap()


    fun init(context: Context) {
        val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        soundPool = SoundPool.Builder()
                .setMaxStreams(3)
                .setAudioAttributes(audioAttributes)
                .build()
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        enabled = prefs.sound
        val s = loadFolder(context, "sounds").toTypedArray()
        for (sound in s) {
            sounds[sound] = loadFile(context, sound)
        }
    }

    fun playSound(soundName: String?) {
        if (!enabled || soundName == null || soundName == "") return
        Thread {
            val volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat() / audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
            val soundID = sounds["sounds/$soundName.mp3"]
            if (soundID != null) {
                soundPool.play(soundID, volume * 2, volume * 2, 0, 0, 1f)
            } else {
                Log.e("IOW", "Unknown sound file: $soundName")
            }
        }.start()
    }

    @Throws(IOException::class)
    private fun loadFolder(context: Context, name: String): List<String> {
        val result: MutableList<String> = ArrayList()
        for (asset in context.assets.list(name)!!) {
            if (asset.contains(".")) {
                result.add("$name/$asset")
            } else {
                result.addAll(loadFolder(context, "$name/$asset"))
            }
        }
        return result
    }

    @Throws(IOException::class)
    private fun loadFile(context: Context, name: String): Int {
        return soundPool.load(context.assets.openFd(name), 1)
    }
}