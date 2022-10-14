package com.agrogames.islandsofwar.render.impl;

import android.os.Build;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import com.agrogames.islandsofwar.engine.abs.bullet.Bullet;
import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.movable.MovableObject;
import com.agrogames.islandsofwar.engine.abs.unit.Unit;
import com.agrogames.islandsofwar.engine.abs.weapon.Weapon;
import com.agrogames.islandsofwar.graphics.abs.TextureBitmap;
import com.agrogames.islandsofwar.graphics.abs.TextureDrawer;
import com.agrogames.islandsofwar.render.abs.Presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Renderer implements com.agrogames.islandsofwar.render.abs.Renderer {
    private final Presenter presenter;
    private Unit selectedUnit;
    private final List<Pair<Unit, Float>> selectable = new ArrayList<>();

    public Renderer(Presenter presenter){
        this.presenter = presenter;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void render(TextureDrawer drawer, Point touch) {
        drawer.DrawTexture(7.5f, 5, TextureBitmap.Background, 15, 10, 0);

        selectable.clear();
        for (Unit unit: presenter.getAttackers()){
            float size = GameObjectRenderer.render(unit, drawer);
            selectable.add(new Pair<>(unit, size));
            for(Weapon weapon: unit.getWeapons()){
                GameObjectRenderer.render(weapon, drawer);
            }
        }
        for (Unit unit: presenter.getProtectors()){
            GameObjectRenderer.render(unit, drawer);
            for(Weapon weapon: unit.getWeapons()){
                GameObjectRenderer.render(weapon, drawer);
            }
        }
        for (Bullet bullet: presenter.getAttackersBullets()){
            GameObjectRenderer.render(bullet, drawer);
        }
        for (Bullet bullet: presenter.getProtectorsBullets()){
            GameObjectRenderer.render(bullet, drawer);
        }

        if (touch != null){
            onTouch(touch);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onTouch(Point touch) {
        Unit su = selectable.stream()
                .filter(u -> u.first.getLocation().x + u.second / 2 > touch.x &&
                        u.first.getLocation().x - u.second / 2 < touch.x &&
                        u.first.getLocation().y + u.second / 2 > touch.y &&
                        u.first.getLocation().y - u.second / 2 < touch.y &&
                u.first instanceof MovableObject)
                .findFirst().orElse(new Pair<>(null, null)).first;
        if(su != null){
            selectedUnit = su;
        } else if(selectedUnit != null){
            MovableObject mo = (MovableObject) selectedUnit;
            mo.setGoal(new Cell(touch));
        }
    }
}
