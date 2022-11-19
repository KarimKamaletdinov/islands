package com.agrogames.islandsofwar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.agrogames.islandsofwar.R;

public class StartActivity extends AppCompatActivity {

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
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("CallingActivity", "StartActivity");
            startActivity(intent);
        });
        findViewById(R.id.settings_button).setOnClickListener((e) -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra("CallingActivity", "StartActivity");
            startActivity(intent);
        });
    }
}