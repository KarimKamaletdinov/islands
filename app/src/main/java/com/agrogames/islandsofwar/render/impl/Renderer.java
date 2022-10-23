package com.agrogames.islandsofwar.render.impl;

import android.os.Build;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import com.agrogames.islandsofwar.engine.abs.bullet.Bullet;
import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.gamevalue.IntValue;
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
    private final UnitList unitList;

    public Renderer(Presenter presenter, UI ui){
        this.presenter = presenter;
        this.ui = ui;
        cancelButton = ui.createElement(ElementType.Button, 14, 9, 1, 1, TextureBitmap.CancelButton);
        cancelButton.setVisible(false);

        unitList = new UnitList(ui);

        cancelButton.onClick(() -> {
            cancelButton.setVisible(false);
            unitList.clearUnits();
            selectedUnit = null;
            return null;
        });

        MapScroller.scroll(-5f, -5f);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void render(TextureDrawer drawer, Point touch, Point move, Point previousMove) {

        MapScroller.start(drawer);
        drawer.drawTexture(15f, 10, TextureBitmap.Background, 30, 20, 0);
        selectable.clear();
        for (Unit unit: presenter.getAttackers()){
            if(unit == selectedUnit){
                GameObjectRenderer.render(unit, drawer, ObjectState.Selected);
                drawHealth(drawer, unit);
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
        MapScroller.finish(drawer);

        if(touch == null) {
            ui.render(drawer, null, null);
        } else if(!ui.render(drawer, touch.x, touch.y)){
            onTouch(MapScroller.convert(touch));
        }
        if(move != null && previousMove != null){
            MapScroller.scroll(move.x - previousMove.x, move.y - previousMove.y);
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
            selectedUnit = su;
            if (su instanceof Transport)
                unitList.setUnits(((Transport) su).getUnits());
            else
                unitList.clearUnits();
        } else if(unitList.getCurrentUnit() != null && selectedUnit != null && selectedUnit instanceof Transport){
            Transport t = (Transport) selectedUnit;
            TransportUnit[] units = t.getUnits();
            if (units.length == 0) {
                unitList.clearUnits();
            } else {
                t.spawn(unitList.getCurrentUnit(), new Cell(touch));
                units = t.getUnits();
                unitList.clearUnits(false);
                unitList.setUnits(units);
            }
        } else if(selectedUnit != null){
            MovableObject mo = (MovableObject) selectedUnit;
            mo.setGoal(new Cell(touch));
        }
    }

    private void drawHealth(TextureDrawer drawer, Unit unit){
        Point l = unit.getLocation();
        IntValue health = unit.getHealth();
        drawer.drawTexture(l.x, l.y + 1, TextureBitmap.Red, health.start / 15f, 0.1f, 0);
        drawer.drawTexture(l.x - (health.start - health.current) / 30f, l.y + 1, TextureBitmap.Green,
                health.current / 15f, 0.1f, 0);
    }
}
