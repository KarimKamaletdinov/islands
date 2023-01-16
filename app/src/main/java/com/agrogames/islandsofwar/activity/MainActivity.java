package com.agrogames.islandsofwar.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.agrogames.islandsofwar.graphics.impl.gl.GLSurfaceView;
import com.agrogames.islandsofwar.rendermanager.Manager;

import java.util.concurrent.Callable;

public class MainActivity extends AppCompatActivity {
    private Manager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        android.opengl.GLSurfaceView gLView = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            manager = new Manager(this, getIntent().getIntExtra("id", -1), true, null, null, null,
                    () -> {
                        finish();
                        return null;
                    });
            gLView = new GLSurfaceView(this, manager);
        }
        setContentView(gLView);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void onBackPressed() {
        manager.stop();
        super.onBackPressed();
    }
}