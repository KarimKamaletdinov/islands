package com.agrogames.islandsofwar.engine.impl.bullet;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.agrogames.islandsofwar.engine.abs.graphics.GraphicsAdder;
import com.agrogames.islandsofwar.engine.abs.bullet.IBullet;
import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObject;
import com.agrogames.islandsofwar.factories.Factory;
import com.agrogames.islandsofwar.map.impl.Water;
import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.bullet.BulletAdder;
import com.agrogames.islandsofwar.engine.abs.map.MapObject;
import com.agrogames.islandsofwar.engine.abs.unit.IUnit;
import com.agrogames.islandsofwar.engine.abs.map.MapProvider;
import com.agrogames.islandsofwar.engine.abs.unit.IUnitAdder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bullet implements IBullet {
    private final String texture;
    private final String creationSound;
    private Point location;
    private float rotation;
    private final float speed;
    private final int power;
    private final float longRange;
    private final int flightHeight;
    private final int targetHeight;
    private final IUnit owner;
    private final boolean bang;
    private final float bangRange;
    private boolean isFlying = true;
    private float flewDistance;
    private Cell goal;

    public Bullet(String texture, String creationSound, Point location, float speed, int power, float longRange, int flightHeight, int targetHeight, IUnit owner, boolean bang, float bangRange) {
        this.creationSound = creationSound;
        this.longRange = longRange;
        this.texture = texture;
        this.location = location;
        this.speed = speed;
        this.power = power;
        this.flightHeight = flightHeight;
        this.targetHeight = targetHeight;
        this.owner = owner;
        this.bang = bang;
        this.bangRange = bangRange;
    }

    @Override
    public String getTexture() {
        return texture;
    }

    @Override
    public boolean hasStopped() {
        return !isFlying;
    }

    @Override
    public Point getLocation() {
        return location;
    }

    @Override
    public float getRotation() {
        return rotation;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void update(MapProvider provider, BulletAdder bulletAdder, IUnitAdder unitAdder, GraphicsAdder graphicsAdder, float deltaTime) {
        move(deltaTime);
        MapObject[] all = provider.getAll();
        MapObject[] enemies = enemyAt(new Cell((int) location.x + 1, (int) location.y + 1), all, flightHeight);
        if(enemies.length != 0){
            for(MapObject enemy : enemies){
                if(enemy instanceof IUnit) ((IUnit)enemy).loseHealth(power);
                stop(provider, graphicsAdder);
                graphicsAdder.addGraphics(Factory.getGraphics("hit", getLocation(), getRotation()));
            }
        }
        else if(new Cell((int) location.x + 1, (int) location.y + 1).equals(goal) || flewDistance >= longRange){
            MapObject[] e2 = enemyAt(new Cell((int) location.x + 1, (int) location.y + 1), all, targetHeight);
            for(MapObject enemy : e2){
                if(enemy instanceof IUnit) ((IUnit)enemy).loseHealth(power);
                graphicsAdder.addGraphics(Factory.getGraphics("hit", getLocation(), getRotation()));
            }
            stop(provider, graphicsAdder);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void stop(MapProvider provider, GraphicsAdder adder){
        isFlying = false;
        if(bang && Arrays.stream(provider.getAll()).noneMatch(o ->
                Arrays.asList(o.getTerritory()).contains(new Cell(location)) && o instanceof Water) && targetHeight == 1){
            adder.addGraphics(Factory.getGraphics("bang", getLocation(), getRotation()));
        }
    }

    private void move(float deltaTime){
        double dx = Math.cos(rotation) * (double) speed * (double) deltaTime;
        double dy = Math.sin(rotation) * (double) speed * (double) deltaTime;
        location = new Point(location.x + (float)dx,  location.y + (float) dy);
        flewDistance += speed * deltaTime;
    }

    private MapObject[] enemyAt(Cell cell, MapObject[] enemies, int height){
        if (bang) {
            List<MapObject> e = new ArrayList<>();
            for (MapObject object : enemies){
                if(object != owner && object.getHeight() == height){
                    Cell[] territory = object.getTerritory();
                    for (Cell c : territory){
                        if(cell.equals(c)){
                            if(!e.contains(object)) e.add(object);
                        }
                    }
                    if(object instanceof RenderableObject){
                        Point l = ((RenderableObject) object).getLocation();
                        if(l.x - bangRange < location.x && l.x + bangRange > location.x &&
                                l.y - bangRange < location.y && l.y + bangRange > location.y){
                            if(!e.contains(object)) e.add(object);
                        }
                    }
                }
            }
            return e.toArray(new MapObject[0]);
        } else {
            for (MapObject object : enemies){
                if(object != owner && object.getHeight() == height){
                    Cell[] territory = object.getTerritory();
                    for (Cell c : territory){
                        if(cell.equals(c)){
                            return new MapObject[]{object};
                        }
                    }
                }
            }
        }
        return new MapObject[0];
    }

    @Override
    public void setGoal(Point goal) {
        this.rotation = (float) Math.atan(
                ((double) goal.y - (double) location.y) /
                        ((double) goal.x - (double) location.x));
        if (goal.x  < location.x){
            rotation += Math.PI;
        }
        this.goal = new Cell(goal);
    }

    @Override
    public String getCreationSound() {
        return creationSound;
    }
}
