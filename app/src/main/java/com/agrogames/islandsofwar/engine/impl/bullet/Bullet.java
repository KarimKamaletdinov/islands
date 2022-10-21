package com.agrogames.islandsofwar.engine.impl.bullet;

import com.agrogames.islandsofwar.types.BulletType;
import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.bullet.BulletAdder;
import com.agrogames.islandsofwar.engine.abs.map.MapObject;
import com.agrogames.islandsofwar.engine.abs.unit.Unit;
import com.agrogames.islandsofwar.engine.abs.map.MapProvider;
import com.agrogames.islandsofwar.engine.abs.unit.UnitAdder;

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
    private boolean isFlying = true;
    private float flewDistance;
    private Cell goal;

    public Bullet(BulletType type, Point location, float speed, int power, float longRange, int flightHeight, int targetHeight, Unit owner) {
        this.longRange = longRange;
        this.type = type;
        this.location = location;
        this.speed = speed;
        this.power = power;
        this.flightHeight = flightHeight;
        this.targetHeight = targetHeight;
        this.owner = owner;
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

    @Override
    public void update(MapProvider provider, BulletAdder bulletAdder, UnitAdder unitAdder, float deltaTime) {
        move(deltaTime);
        MapObject[] all = provider.getAll();
        MapObject enemy = enemyAt(new Cell((int) location.x + 1, (int) location.y + 1), all, flightHeight);
        if(enemy != null){
            if(enemy instanceof Unit) ((Unit)enemy).loseHealth(power);
            isFlying = false;
        }
        else if(new Cell((int) location.x + 1, (int) location.y + 1).equals(goal)){
            MapObject e2 = enemyAt(new Cell((int) location.x + 1, (int) location.y + 1), all, targetHeight);
            if(e2 != null){
                if(e2 instanceof Unit) ((Unit)e2).loseHealth(power);
            }
            isFlying = false;
        }
        else if(flewDistance >= longRange){
            isFlying = false;
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
