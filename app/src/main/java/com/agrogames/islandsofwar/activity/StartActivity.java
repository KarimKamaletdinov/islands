package com.agrogames.islandsofwar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.agrogames.islandsofwar.R;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StartActivity extends AppCompatActivity {

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        findViewById(R.id.play).setOnClickListener((e) -> {
            Intent intent = new Intent(this, SelectIslandActivity.class);
            intent.putExtra("CallingActivity", "StartActivity");
            intent.putExtra("id", 1);
            startActivity(intent);
        });
        findViewById(R.id.settings_button).setOnClickListener((e) -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra("CallingActivity", "StartActivity");
            startActivity(intent);
        });
    }
}