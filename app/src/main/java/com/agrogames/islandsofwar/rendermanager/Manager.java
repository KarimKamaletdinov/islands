package com.agrogames.islandsofwar.rendermanager;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.agrogames.islandsofwar.engine.abs.Engine;
import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.unit.Unit;
import com.agrogames.islandsofwar.engine.abs.movable.MovableObject;
import com.agrogames.islandsofwar.engine.impl.unit.UnitFactory;
import com.agrogames.islandsofwar.graphics.abs.TextureDrawer;
import com.agrogames.islandsofwar.graphics.abs.RenderManager;
import com.agrogames.islandsofwar.map.impl.Map;
import com.agrogames.islandsofwar.render.impl.Renderer;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Manager implements RenderManager {
    private final Engine engine;
    private final Renderer renderer;
    private LocalTime previous;

    public Manager(Context context) {
        Unit tank1 = UnitFactory.Tank(5, 4);
        Unit tank2 = UnitFactory.Tank(6, 4);
        Unit tank3 = UnitFactory.Tank(10, 4);
        this.engine = new com.agrogames.islandsofwar.engine.impl.Engine(new Unit[]{
                tank1,
                tank2,
        }, new Unit[]{
                tank3,
        }, Map.fromAssets(context, "map1.txt").getMap());
        this.renderer = new Renderer(new Presenter(this.engine));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void Render(TextureDrawer drawer) {
        if (previous == null) {
            previous = LocalTime.now();
        }
        float deltaTime = ((float)previous.until(LocalTime.now(), ChronoUnit.MICROS)) / 1000000f;
        previous = LocalTime.now();
        engine.update(deltaTime);
        renderer.render(drawer);
    }
}
