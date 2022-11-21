package com.agrogames.islandsofwar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.agrogames.islandsofwar.R;
import com.agrogames.islandsofwar.common.Arr;
import com.agrogames.islandsofwar.factories.Factory;
import com.agrogames.islandsofwar.graphics.impl.gl.GLSurfaceView;
import com.agrogames.islandsofwar.islands.abs.Island;
import com.agrogames.islandsofwar.islands.impl.LocalIslandProvider;
import com.agrogames.islandsofwar.islands.impl.LocalUserProvider;
import com.agrogames.islandsofwar.rendermanager.Manager;

public class SelectIslandActivity extends AppCompatActivity {
    private Manager manager;
    private GLSurfaceView gLView;
    private int[] ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Factory.load(this);
        Island[] attackable = new LocalIslandProvider(this).getAttackable();
        ids = new int[attackable.length];
        for (int i = 0, attackableLength = attackable.length; i < attackableLength; i++) {
            Island island = attackable[i];
            ids[i] = island.id;
        }
        startView(1);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void startView(int id) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            manager = new Manager(this,
                    id,
                    false,
                    id == ids[ids.length - 1] ? null : () -> {
                        startView(ids[Arr.indexOf(ids, id) + 1]);
                        return null;
                    },
                    id == ids[0] ? null : () -> {
                        startView(ids[Arr.indexOf(ids, id) - 1]);
                        return null;
                    },
                    () -> {
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra("CallingActivity", "StartActivity");
                        intent.putExtra("id", id);
                        startActivity(intent);
                        return null;
                    }, null);
            if(gLView == null){
                gLView = new GLSurfaceView(this, manager);
                setContentView(gLView);
            } else {
                gLView.setManager(manager);
            }
        }
    }

    @Override
    public void onBackPressed() {
        manager.stop();
        super.onBackPressed();
    }
}