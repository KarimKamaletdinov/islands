package com.agrogames.islandsofwar.rendermanager;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.agrogames.islandsofwar.engine.abs.Engine;
import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.game.GameObject;
import com.agrogames.islandsofwar.engine.abs.game.MovableObject;
import com.agrogames.islandsofwar.engine.impl.unit.UnitFactory;
import com.agrogames.islandsofwar.graphics.abs.TextureDrawer;
import com.agrogames.islandsofwar.graphics.abs.RenderManager;
import com.agrogames.islandsofwar.map.impl.Map;
import com.agrogames.islandsofwar.render.impl.Renderer;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class Manager implements RenderManager {
    private final Engine engine;
    private final Renderer renderer;
    private LocalTime previous;

    public Manager(Context context) {
        GameObject tank1 = UnitFactory.Tank(5, 5);
        GameObject tank2 = UnitFactory.Tank(10, 5);
        GameObject tank3 = UnitFactory.Tank(10, 4);
        ((MovableObject)tank1).setGoal(new Cell(10, 1));
        ((MovableObject)tank2).setGoal(new Cell(5, 1));
        this.engine = new com.agrogames.islandsofwar.engine.impl.Engine(new GameObject[]{
                tank1,
                tank2,
        }, new GameObject[]{
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
