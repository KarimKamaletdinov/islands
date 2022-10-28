package com.agrogames.islandsofwar.engine.impl.bullet;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.agrogames.islandsofwar.engine.abs.another.AnotherAdder;
import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObject;
import com.agrogames.islandsofwar.factories.AnotherObjectFactory;
import com.agrogames.islandsofwar.map.impl.Water;
import com.agrogames.islandsofwar.types.BulletType;
import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.bullet.BulletAdder;
import com.agrogames.islandsofwar.engine.abs.map.MapObject;
import com.agrogames.islandsofwar.engine.abs.unit.Unit;
import com.agrogames.islandsofwar.engine.abs.map.MapProvider;
import com.agrogames.islandsofwar.engine.abs.unit.UnitAdder;

import java.util.Arrays;

public class Bullet implements com.agrogames.islandsofwar.engine.abs.bullet.Bullet {
    private final BulletType type;
    private Point location;
    private float rotation;
    private final float speed;
    private final int power;
    private final float longRange;
    private final int flightHeight;
    private final int targetHeight;
    private final Unit owner;
    private final boolean bang;
    private boolean isFlying = true;
    private float flewDistance;
    private Cell goal;

    public Bullet(BulletType type, Point location, float speed, int power, float longRange, int flightHeight, int targetHeight, Unit owner, boolean bang) {
        this.longRange = longRange;
        this.type = type;
        this.location = location;
        this.speed = speed;
        this.power = power;
        this.flightHeight = flightHeight;
        this.targetHeight = targetHeight;
        this.owner = owner;
        this.bang = bang;
    }

    @Override
    public BulletType getType() {
        return type;
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
    public void update(MapProvider provider, BulletAdder bulletAdder, UnitAdder unitAdder, AnotherAdder anotherAdder, float deltaTime) {
        move(deltaTime);
        MapObject[] all = provider.getAll();
        MapObject enemy = enemyAt(new Cell((int) location.x + 1, (int) location.y + 1), all, flightHeight);
        if(enemy != null){
            if(enemy instanceof Unit) ((Unit)enemy).loseHealth(power);
            stop(provider, anotherAdder);
        }
        else if(new Cell((int) location.x + 1, (int) location.y + 1).equals(goal)){
            MapObject e2 = enemyAt(new Cell((int) location.x + 1, (int) location.y + 1), all, targetHeight);
            if(e2 != null){
                if(e2 instanceof Unit) ((Unit)e2).loseHealth(power);
            }
            stop(provider, anotherAdder);
        }
        else if(flewDistance >= longRange){
            stop(provider, anotherAdder);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void stop(MapProvider provider, AnotherAdder adder){
        isFlying = false;
        if(bang && Arrays.stream(provider.getAll()).noneMatch(o ->
                Arrays.asList(o.getTerritory()).contains(new Cell(location)) && o instanceof Water)){
            adder.addAnother(AnotherObjectFactory.bang(this));
        }
    }

    private void move(float deltaTime){
        double dx = Math.cos(rotation) * (double) speed * (double) deltaTime;
        double dy = Math.sin(rotation) * (double) speed * (double) deltaTime;
        location = new Point(location.x + (float)dx,  location.y + (float) dy);
        flewDistance += speed * deltaTime;
    }

    private MapObject enemyAt(Cell cell, MapObject[] enemies, int height){
        for (MapObject object : enemies){
            if(object != owner && object.getHeight() == height){
                Cell[] territory = object.getTerritory();
                for (Cell c : territory){
                    if(cell.equals(c)){
                        return object;
                    }
                }
                if(bang && object instanceof RenderableObject){
                    Point l = ((RenderableObject) object).getLocation();
                    if(l.x - 0.8f < location.x && l.x + 0.8f > location.x &&
                            l.y - 0.8f < location.y && l.y + 0.8f > location.y){
                        return object;
                    }
                }
            }
        }
        return null;
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
}
