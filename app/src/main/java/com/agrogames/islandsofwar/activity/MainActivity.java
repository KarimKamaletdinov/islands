package com.agrogames.islandsofwar.activity;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.agrogames.islandsofwar.R;
import com.agrogames.islandsofwar.graphics.impl.gl.GLSurfaceView;
import com.agrogames.islandsofwar.rendermanager.Manager;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public SoundPool soundPool;
    public int sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        android.opengl.GLSurfaceView gLView = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            gLView = new GLSurfaceView(this, new Manager(this));
        }
        setContentView(gLView);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        //mediaPlayer = MediaPlayer.create(this, getResources().getIdentifier("sample1", "raw", "com.agrogames.islandsofwar"));
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
        try {
            sound = soundPool.load(getAssets().openFd("sounds/landing.aac"), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}