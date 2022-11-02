package com.agrogames.islandsofwar.rendermanager;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.agrogames.islandsofwar.engine.abs.Engine;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.unit.IUnit;
import com.agrogames.islandsofwar.factories.UnitFactory;
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
    private Point move;
    private Point previousMove;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Manager(Context context) {
        IUnit ts = UnitFactory.byTexture("transport_ship", 1, 1,
                "tank",
                "tank",
                "tank",
                "tank",
                "tank",
                "tank",
                "tank",
                "rocket_launcher");

        this.engine = new com.agrogames.islandsofwar.engine.impl.Engine(new IUnit[]{
                UnitFactory.byTexture("tank", 14, 11),
                UnitFactory.byTexture("tank", 15, 10),
                UnitFactory.byTexture("tank", 15, 9),
                UnitFactory.byTexture("tank", 15, 11),
                UnitFactory.byTexture("tank", 16, 10),
                UnitFactory.byTexture("tank", 16, 9),
                UnitFactory.byTexture("rocket_launcher", 16, 11),
                UnitFactory.byTexture("rocket_launcher", 16, 12),
                UnitFactory.byTexture("tank", 17, 10),
                UnitFactory.byTexture("tank", 17, 9),
                UnitFactory.byTexture("tank", 17, 11),
                UnitFactory.byTexture("tank", 20, 10),
                UnitFactory.byTexture("air_defence", 15, 12),
                UnitFactory.byTexture("ship_defence", 8, 13),
                UnitFactory.byTexture("ship_defence", 18, 12),
                UnitFactory.byTexture("ship_defence", 17, 8),
                UnitFactory.byTexture("ship_defence", 14, 10),
        }, new IUnit[]{
                ts
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
    public void render(TextureDrawer drawer) {
        start = true;
        renderer.render(drawer, touch, move, previousMove);
        touch = null;
        previousMove = move;
        move = null;
    }

    @Override
    public void onTouch(float x, float y) {
        touch = new Point(x, y);
    }

    @Override
    public void onMove(float x, float y) {
        move = new Point(x, y);
    }
}
