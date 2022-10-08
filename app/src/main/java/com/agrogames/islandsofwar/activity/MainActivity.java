package com.agrogames.islandsofwar.activity;

import android.os.Bundle;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import com.agrogames.islandsofwar.graphics.impl.gl.GLSurfaceView;
import com.agrogames.islandsofwar.rendermanager.Manager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        android.opengl.GLSurfaceView gLView = new GLSurfaceView(this, new Manager(this));
        setContentView(gLView);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

    }
}