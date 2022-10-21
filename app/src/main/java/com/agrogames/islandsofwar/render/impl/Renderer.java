package com.agrogames.islandsofwar.render.impl;

import android.os.Build;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import com.agrogames.islandsofwar.engine.abs.bullet.Bullet;
import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.movable.MovableObject;
import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObject;
import com.agrogames.islandsofwar.engine.abs.transport.Transport;
import com.agrogames.islandsofwar.engine.abs.transport.TransportUnit;
import com.agrogames.islandsofwar.engine.abs.unit.Unit;
import com.agrogames.islandsofwar.engine.abs.weapon.Weapon;
import com.agrogames.islandsofwar.types.TextureBitmap;
import com.agrogames.islandsofwar.graphics.abs.TextureDrawer;
import com.agrogames.islandsofwar.render.abs.Presenter;
import com.agrogames.islandsofwar.ui.abs.Element;
import com.agrogames.islandsofwar.ui.abs.ElementType;
import com.agrogames.islandsofwar.ui.abs.UI;

import java.util.ArrayList;
import java.util.List;

public class Renderer implements com.agrogames.islandsofwar.render.abs.Renderer {
    private final Presenter presenter;
    private final UI ui;
    private Unit selectedUnit;
    private final List<Pair<Unit, Float>> selectable = new ArrayList<>();
    private final Element cancelButton;
    private final Element spawnButton;
    private boolean spawn;

    public Renderer(Presenter presenter, UI ui){
        this.presenter = presenter;
        this.ui = ui;
        cancelButton = ui.createElement(ElementType.Button, 14, 9, 1, 1, TextureBitmap.CancelButton);
        cancelButton.setVisible(false);

        spawnButton = ui.createElement(ElementType.Button, 14, 8, 1, 1, TextureBitmap.LandUnitsButton);
        spawnButton.setVisible(false);

        cancelButton.onClick(() -> {
            cancelButton.setVisible(false);
            spawnButton.setVisible(false);
            selectedUnit = null;
            spawn = false;
            spawnButton.setTexture(TextureBitmap.LandUnitsButton);
            return null;
        });
        spawnButton.onClick(() -> {
            if(selectedUnit != null && selectedUnit instanceof Transport){
                if(spawn){
                    spawn = false;
                    spawnButton.setTexture(TextureBitmap.LandUnitsButton);
                } else {
                    spawn = true;
                    spawnButton.setTexture(TextureBitmap.LandUnitsButtonSelected);
                }
            }
            return null;
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void render(TextureDrawer drawer, Point touch) {
        drawer.DrawTexture(7.5f, 5, TextureBitmap.Background, 15, 10, 0);

        selectable.clear();
        for (Unit unit: presenter.getAttackers()){
            if(unit == selectedUnit){
                GameObjectRenderer.render(unit, drawer, ObjectState.Selected);
            } else{
                float size = GameObjectRenderer.render(unit, drawer);
                selectable.add(new Pair<>(unit, size));
            }
        }
        for (Unit unit: presenter.getProtectors()){
            GameObjectRenderer.render(unit, drawer);
        }
        for (RenderableObject r: presenter.getOther()){
            GameObjectRenderer.render(r, drawer, ObjectState.Destroyed);
        }

        for (Bullet bullet: presenter.getAttackersBullets()){
            GameObjectRenderer.render(bullet, drawer);
        }
        for (Bullet bullet: presenter.getProtectorsBullets()){
            GameObjectRenderer.render(bullet, drawer);
        }

        for (Unit unit: presenter.getAttackers()){
            for(Weapon weapon: unit.getWeapons()){
                GameObjectRenderer.render(weapon, drawer);
            }
        }
        for (Unit unit: presenter.getProtectors()){
            for(Weapon weapon: unit.getWeapons()){
                GameObjectRenderer.render(weapon, drawer);
            }
        }

        if(touch == null) {
            ui.render(drawer, null, null);
        } else {
            if(!ui.render(drawer, touch.x, touch.y)){
                onTouch(touch);
            }
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
            cancelButton.setVisible(true);
            spawnButton.setVisible(su instanceof Transport);
            selectedUnit = su;
        } else if(spawn && selectedUnit != null && selectedUnit instanceof Transport){
            Transport t = (Transport) selectedUnit;
            TransportUnit[] units = t.getUnits();
            if (units.length == 0) {
                spawn = false;
                spawnButton.setTexture(TextureBitmap.LandUnitsButton);
                spawnButton.setVisible(false);
            } else {
                t.spawn(units[0], new Cell(touch));
            }
        } else if(selectedUnit != null){
            MovableObject mo = (MovableObject) selectedUnit;
            mo.setGoal(new Cell(touch));
        }
    }
}
