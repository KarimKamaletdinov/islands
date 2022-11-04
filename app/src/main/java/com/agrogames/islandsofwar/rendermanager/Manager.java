package com.agrogames.islandsofwar.rendermanager;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.agrogames.islandsofwar.engine.abs.Engine;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.unit.IUnit;
import com.agrogames.islandsofwar.factories.Factory;
import com.agrogames.islandsofwar.graphics.abs.TextureDrawer;
import com.agrogames.islandsofwar.graphics.abs.RenderManager;
import com.agrogames.islandsofwar.map.impl.Map;
import com.agrogames.islandsofwar.render.abs.Renderer;
import com.agrogames.islandsofwar.ui.impl.UI;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Manager implements RenderManager {
    private final Engine engine;
    private final Renderer renderer;
    private LocalTime previous;
    private LocalTime renderPrevious;
    private boolean start = false;
    private Point touch;
    private Point move;
    private Point previousMove;
    private final List<Float> deltaTimes = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Manager(Context context) {
        Factory.load(context);

        IUnit ts = Factory.get("transport_ship", 1, 1//,
                //"tank",
                //"tank",
                //"tank",
                //"tank",
                //"tank",
                //"tank",
                //"tank",
                //"rocket_launcher"
        );

        this.engine = new com.agrogames.islandsofwar.engine.impl.Engine(new IUnit[]{
                Factory.get("tank", 14, 11),
                Factory.get("tank", 15, 10),
                Factory.get("tank", 15, 9),
                Factory.get("tank", 15, 11),
                Factory.get("tank", 16, 10),
                Factory.get("tank", 16, 9),
                Factory.get("rocket_launcher", 16, 11),
                Factory.get("rocket_launcher", 16, 12),
                Factory.get("tank", 17, 10),
                Factory.get("tank", 17, 9),
                Factory.get("tank", 17, 11),
                Factory.get("tank", 20, 10),/*
                UnitFactory.byTexture("air_defence", 15, 12),
                UnitFactory.byTexture("ship_defence", 8, 13),
                UnitFactory.byTexture("ship_defence", 18, 12),
                UnitFactory.byTexture("ship_defence", 17, 8),
                UnitFactory.byTexture("ship_defence", 14, 10),*/
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
                    //if(deltaTime > 0.1f) deltaTime = 0.1f;
                    previous = now;
                    engine.update(deltaTime);
                }
            }
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void render(TextureDrawer drawer) {
        LocalTime now = LocalTime.now();
        if (renderPrevious == null) {
            renderPrevious = now;
        }
        float deltaTime = ((float)renderPrevious.until(now, ChronoUnit.MICROS)) / 1000000f;
        //if(deltaTime > 0.1f) deltaTime = 0.1f;
        renderPrevious = now;
        deltaTimes.add(deltaTime);
        if(deltaTimes.size() >= 100){
            float sum = 0;
            for (float n: deltaTimes) {
                sum += n;
            }
            sum /= deltaTimes.size();
            Log.i("IOW", "Medium deltaTime:" + sum);
            deltaTimes.clear();
        }
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
