package com.agrogames.islandsofwar.rendermanager;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.agrogames.islandsofwar.engine.abs.Engine;
import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.unit.Unit;
import com.agrogames.islandsofwar.engine.abs.movable.MovableObject;
import com.agrogames.islandsofwar.engine.impl.unit.UnitFactory;
import com.agrogames.islandsofwar.graphics.abs.TextureDrawer;
import com.agrogames.islandsofwar.graphics.abs.RenderManager;
import com.agrogames.islandsofwar.map.impl.Map;
import com.agrogames.islandsofwar.render.abs.Renderer;
import com.agrogames.islandsofwar.ui.impl.UI;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Manager implements RenderManager {
    private final Engine engine;
    private final Renderer renderer;
    private LocalTime previous;
    private boolean start = false;
    private Point touch;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Manager(Context context) {
        Unit tank1 = UnitFactory.Tank(4, 5);
        Unit tank2 = UnitFactory.Tank(5, 5);
        ((MovableObject) tank1).setGoal(new Cell(new Point(9, 5)));
        Unit tank3 = UnitFactory.Tank(5, 4);
        Unit tank4 = UnitFactory.Tank(5, 6);
        Unit tank5 = UnitFactory.Tank(10, 5);
        this.engine = new com.agrogames.islandsofwar.engine.impl.Engine(new Unit[]{
                tank5
        }, new Unit[]{
                tank1,
                tank2,
                tank3,
                tank4,
        }, Map.fromAssets(context, "map1.txt").getMap());
        this.renderer = new com.agrogames.islandsofwar.render.impl.Renderer(new Presenter(this.engine), new UI());

        new Thread(() -> {
            while (true){
                if(start){
                    LocalTime now = LocalTime.now();
                    if (previous == null) {
                        previous = now;
                    }
                    float deltaTime = ((float)previous.until(now, ChronoUnit.MICROS)) / 1000000f;
                    if(deltaTime > 0.1f) deltaTime = 0.1f;
                    previous = now;
                    engine.update(deltaTime);
                }
            }
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void Render(TextureDrawer drawer) {
        renderer.render(drawer, touch);
        touch = null;
    }

    @Override
    public void OnTouch(float x, float y) {
        start = true;
        touch = new Point(x, y);
    }
}
