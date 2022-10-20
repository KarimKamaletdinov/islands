package com.agrogames.islandsofwar.rendermanager;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.agrogames.islandsofwar.engine.abs.Engine;
import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.transport.TransportUnit;
import com.agrogames.islandsofwar.engine.abs.unit.Unit;
import com.agrogames.islandsofwar.engine.abs.movable.MovableObject;
import com.agrogames.islandsofwar.engine.abs.unit.UnitType;
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
        Unit ts = UnitFactory.TransportShip(1, 1, new TransportUnit[]{
                new TransportUnit(UnitType.Tank),
                new TransportUnit(UnitType.Tank),
                new TransportUnit(UnitType.Tank),
                new TransportUnit(UnitType.Tank),
                new TransportUnit(UnitType.Tank),
                new TransportUnit(UnitType.Tank),
                new TransportUnit(UnitType.Tank),
                new TransportUnit(UnitType.Tank),
        });

        this.engine = new com.agrogames.islandsofwar.engine.impl.Engine(new Unit[]{
                UnitFactory.Tank(4, 5),
                UnitFactory.Tank(5, 5),
                UnitFactory.Tank(5, 4),
                UnitFactory.Tank(5, 6),
                UnitFactory.Tank(6, 5),
                UnitFactory.Tank(6, 4),
                UnitFactory.Tank(6, 6),
                UnitFactory.Tank(7, 5),
                UnitFactory.Tank(7, 4),
                UnitFactory.Tank(7, 6),
                UnitFactory.Tank(10, 5),
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
    public void Render(TextureDrawer drawer) {
        start = true;
        renderer.render(drawer, touch);
        touch = null;
    }

    @Override
    public void OnTouch(float x, float y) {
        touch = new Point(x, y);
    }
}
