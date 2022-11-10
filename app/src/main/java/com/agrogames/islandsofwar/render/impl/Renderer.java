package com.agrogames.islandsofwar.render.impl;

import android.os.Build;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import com.agrogames.islandsofwar.engine.abs.GameState;
import com.agrogames.islandsofwar.engine.abs.bullet.IBullet;
import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.gamevalue.IntValue;
import com.agrogames.islandsofwar.engine.abs.movable.MovableObject;
import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObject;
import com.agrogames.islandsofwar.engine.abs.transport.Transport;
import com.agrogames.islandsofwar.engine.abs.transport.TransportUnit;
import com.agrogames.islandsofwar.engine.abs.unit.IUnit;
import com.agrogames.islandsofwar.engine.abs.weapon.IWeapon;
import com.agrogames.islandsofwar.factories.Factory;
import com.agrogames.islandsofwar.graphics.abs.TextureDrawer;
import com.agrogames.islandsofwar.render.abs.Presenter;
import com.agrogames.islandsofwar.ui.abs.Element;
import com.agrogames.islandsofwar.ui.abs.ElementType;
import com.agrogames.islandsofwar.ui.abs.UI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Renderer implements com.agrogames.islandsofwar.render.abs.Renderer {
    private final Presenter presenter;
    private final UI ui;
    private IUnit selectedUnit;
    private final List<Pair<IUnit, Float>> selectable = new ArrayList<>();
    private final Element cancelButton;
    private final UnitList landingList;
    private final UnitList planeList;

    public Renderer(Presenter presenter, UI ui){
        this.presenter = presenter;
        this.ui = ui;
        cancelButton = ui.createElement(ElementType.Button, 14, 9, 1, 1, "ui/cancel_button");
        cancelButton.setVisible(false);

        landingList = new UnitList(ui, 14);

        cancelButton.onClick(() -> {
            cancelButton.setVisible(false);
            landingList.clearUnits();
            selectedUnit = null;
            return null;
        });

        planeList = new UnitList(ui, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            planeList.setUnits(new TransportUnit[]{
                    new TransportUnit(c -> Factory.get("bomber", c.x, c.y))
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void render(TextureDrawer drawer, Point touch, Point move, Point previousMove,
                       Point zoom1, Point zoom2, Point previousZoom1, Point previousZoom2) {
        MapScroller.start(drawer);
        drawer.drawTexture(15f, 10, "other/background", 30, 20, 0);
        selectable.clear();
        ObjectRenderer renderer = new ObjectRenderer(drawer);

        for (RenderableObject r: presenter.getOther()){
            if(r instanceof IUnit)
                renderer.render(r, "destroyed");
            else
                renderer.render(r);
        }
        for (IUnit unit: Arrays.stream(presenter.getAttackers()).filter(c -> c.getHeight() < 3).toArray(IUnit[]::new)){
        //for (IUnit unit: presenter.getAttackers()){
            if(unit == selectedUnit){
                renderer.render(unit, "selected");
                drawHealth(drawer, unit);
            } else{
                float size = renderer.render(unit, "normal");
                selectable.add(new Pair<>(unit, size));
            }
        }
        for (IUnit unit: presenter.getProtectors()){
            renderer.render(unit, "normal");
        }

        for (IBullet bullet: presenter.getAttackersBullets()){
            renderer.render(bullet);
        }
        for (IBullet bullet: presenter.getProtectorsBullets()){
            renderer.render(bullet);
        }

        for (IUnit unit: presenter.getAttackers()){
            for(IWeapon weapon: unit.getWeapons()){
                renderer.render(weapon);
            }
        }
        for (IUnit unit: presenter.getProtectors()){
            for(IWeapon weapon: unit.getWeapons()){
                renderer.render(weapon);
            }
        }
        for (IUnit unit: Arrays.stream(presenter.getAttackers()).filter(c -> c.getHeight() >= 3).toArray(IUnit[]::new)){
            renderer.render(unit, "normal");
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
        if(zoom1 != null && zoom2 != null && previousZoom1 != null && previousZoom2 != null){
            MapScroller.zoom(zoom1, zoom2, previousZoom1, previousZoom2);
        }


        if(presenter.getState() == GameState.Win)
            drawer.drawTexture(7.5f, 5f, "other/win", 0);
        else if(presenter.getState() == GameState.Defeat)
            drawer.drawTexture(7.5f, 5f, "other/defeat", 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onTouch(Point touch) {
        IUnit su = selectable.stream()
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
                landingList.setUnits(((Transport) su).getUnits());
            else
                landingList.clearUnits();
        } else if(landingList.getCurrentUnit() != null && selectedUnit != null && selectedUnit instanceof Transport){
            Transport t = (Transport) selectedUnit;
            TransportUnit[] units = t.getUnits();
            if (units.length == 0) {
                landingList.clearUnits();
            } else {
                t.spawn(landingList.getCurrentUnit(), new Cell(touch));
                units = t.getUnits();
                landingList.clearUnits(false);
                landingList.setUnits(units);
            }
        } else if(planeList.getCurrentUnit() != null){
            IUnit bomber = planeList.getCurrentUnit().create.apply(new Cell(-1, -1));
            MovableObject mo = (MovableObject) bomber;
            mo.setGoal(new Cell(touch));
            presenter.addPlane(bomber);
            planeList.clearUnits();
        } else if(selectedUnit != null){
            MovableObject mo = (MovableObject) selectedUnit;
            mo.setGoal(new Cell(touch));
        }
    }

    private void drawHealth(TextureDrawer drawer, IUnit unit){
        Point l = unit.getLocation();
        IntValue health = unit.getHealth();
        drawer.drawTexture(l.x, l.y + 1, "other/red", health.start / 15f, 0.1f, 0);
        drawer.drawTexture(l.x - (health.start - health.current) / 30f, l.y + 1, "other/green",
                health.current / 15f, 0.1f, 0);
    }
}
