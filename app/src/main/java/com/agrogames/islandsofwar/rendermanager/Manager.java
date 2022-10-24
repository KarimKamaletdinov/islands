package com.agrogames.islandsofwar.rendermanager;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.agrogames.islandsofwar.engine.abs.Engine;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.transport.TransportUnit;
import com.agrogames.islandsofwar.engine.abs.unit.Unit;
import com.agrogames.islandsofwar.types.UnitType;
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
        Unit ts = UnitFactory.TransportShip(1, 1, new TransportUnit[]{
                new TransportUnit(UnitType.Tank),
                new TransportUnit(UnitType.Tank),
                new TransportUnit(UnitType.Tank),
                new TransportUnit(UnitType.Tank),
                new TransportUnit(UnitType.Tank),
                new TransportUnit(UnitType.Tank),
                new TransportUnit(UnitType.Tank),
                new TransportUnit(UnitType.RocketLauncher),
        });

        this.engine = new com.agrogames.islandsofwar.engine.impl.Engine(new Unit[]{
                UnitFactory.Tank(14, 10),
                UnitFactory.Tank(15, 10),
                UnitFactory.Tank(15, 9),
                UnitFactory.Tank(15, 11),
                UnitFactory.Tank(16, 10),
                UnitFactory.Tank(16, 9),
                UnitFactory.RocketLauncher(16, 11),
                UnitFactory.RocketLauncher(16, 12),
                UnitFactory.Tank(17, 10),
                UnitFactory.Tank(17, 9),
                UnitFactory.Tank(17, 11),
                UnitFactory.Tank(20, 10),
        }, new Unit[]{
                ts,
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
