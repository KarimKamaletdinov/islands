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
    private boolean start = false;
    private Point touch;
    private Point move;
    private Point previousMove;
    private Point zoom1;
    private Point previousZoom1;
    private Point zoom2;
    private Point previousZoom2;
    private final List<Float> deltaTimes = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Manager(Context context) {
        Factory.load(context);

        IUnit ts = Factory.get("transport_ship", 1, 1,
                "tank",
                "tank",
                "tank",
                "tank",
                "tank",
                "tank",
                "tank",
                "rocket_launcher"
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
                Factory.get("tank", 20, 10),
                Factory.get("air_defence", 15, 12),
                Factory.get("ship_defence", 8, 13),
                Factory.get("ship_defence", 18, 12),
                Factory.get("ship_defence", 17, 8),
                Factory.get("ship_defence", 14, 10),
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
        start = true;
        renderer.render(drawer, touch, move, previousMove, zoom1, zoom2, previousZoom1, previousZoom2);
        touch = null;
        previousMove = move;
        previousZoom1 = zoom1;
        previousZoom2 = zoom2;
        move = null;
        zoom1 = null;
        zoom2 = null;
    }

    @Override
    public void onTouch(float x, float y) {
        touch = new Point(x, y);
    }

    @Override
    public void onMove(float x, float y) {
        move = new Point(x, y);
    }

    @Override
    public void onZoom(float x1, float y1, float x2, float y2) {
        zoom1 = new Point(x1, y1);
        zoom2 = new Point(x2, y2);
    }
}
