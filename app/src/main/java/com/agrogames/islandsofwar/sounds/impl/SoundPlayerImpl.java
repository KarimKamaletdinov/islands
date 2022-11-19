package com.agrogames.islandsofwar.sounds.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.agrogames.islandsofwar.sounds.abs.SoundPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SoundPlayerImpl implements SoundPlayer {
    private boolean enabled;
    private final SoundPool soundPool;
    private final AudioManager audioManager;
    private final Map<String, Integer> sounds = new HashMap<>();

    public SoundPlayerImpl(Context context){
        AudioAttributes audioAttributes = new AudioAttributes
                .Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool
                .Builder()
                .setMaxStreams(3)
                .setAudioAttributes(audioAttributes)
                .build();
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        enabled = prefs.getBoolean("sound", true);

        try {
            String[] s = loadFolder(context, "sounds").toArray(new String[0]);
            for(String sound: s){
                sounds.put(sound, loadFile(context, sound));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void playSound(String soundName) {
        if(!enabled) return;
        new Thread(() -> {
            float volume = (float)audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) / (float)audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            Integer soundID = sounds.get("sounds/" + soundName + ".mp3");
            if(soundID != null){
                soundPool.play(soundID, volume, volume, 0, 0, 1);
            } else {
                 Log.e("IOW", "Unknown sound file: " + soundName);
            }
        }).start();
    }

    private List<String> loadFolder(Context context, String name) throws IOException {
        List<String> result = new ArrayList<>();
        for (String asset: context.getAssets().list(name)) {
            if(asset.contains(".")){
                result.add(name + "/" + asset);
            } else {
                result.addAll(loadFolder(context, name + "/" + asset));
            }
        }
        return result;
    }

    private int loadFile(Context context, String name) throws IOException{
        return soundPool.load(context.getAssets().openFd(name), 1);
    }

    @Override
    public void disable(){
        enabled = false;
    }
}
