package com.agrogames.islandsofwar.engine.impl.unit;

import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.types.UnitType;
import com.agrogames.islandsofwar.engine.abs.movable.MovableObject;
import com.agrogames.islandsofwar.engine.abs.weapon.Weapon;
import com.agrogames.islandsofwar.engine.abs.gamevalue.IntValue;

abstract class Unit implements com.agrogames.islandsofwar.engine.abs.unit.Unit, MovableObject {
    private final UnitType type;
    protected final IntValue health;
    protected final float speed;
    protected final float rotationSpeed;
    protected Point location;
    protected float rotation;
    protected float goalRotation;
    protected int minDamage = 0;
    private final Weapon[] weapons;

    protected Unit(UnitType type, Cell location, Weapon[] weapons, int health, float speed, float rotationSpeed) {
        this.type = type;
        this.health = new IntValue(health);
        this.weapons = weapons;
        this.location = new Point(location);
        this.speed = speed;
        this.rotationSpeed = rotationSpeed;

        for (Weapon weapon: weapons){
            weapon.setOwner(this);
        }
    }

    @Override
    public UnitType getType() {
        return type;
    }

    @Override
    public IntValue getHealth() {
        return health;
    }

    @Override
    public void loseHealth(int lost) {
        if(lost >= minDamage){
            health.current -= lost;
        }
    }

    @Override
    public Point getLocation() {
        return location;
    }

    public Weapon[] getWeapons(){
        return weapons;
    }

    @Override
    public float getRotation() {
        return rotation;
    }

    protected void rotate(float deltaTime){
        if(rotation == goalRotation) return;
        if(rotation - goalRotation > Math.PI){
            rotation -= Math.PI * 2f;
        } else if(goalRotation - rotation > Math.PI){
            rotation += Math.PI * 2f;
        }
        if(rotation < goalRotation){
            rotation += Math.PI * deltaTime * rotationSpeed;
            if(rotation > goalRotation){
                rotation = goalRotation;
            }
        }
        else{
            rotation -= Math.PI * deltaTime * rotationSpeed;
            if(rotation < goalRotation){
                rotation = goalRotation;
            }
        }
    }

    protected void setRotation(Cell goal) {
        Point g = new Point(goal);
        goalRotation = (float) Math.atan(
                ((double) g.y - (double) location.y) /
                ((double) g.x - (double) location.x));
        if (g.x < location.x){
            goalRotation += Math.PI;
        }
        if(goalRotation > Math.PI * 2f){
            goalRotation -= (float) (Math.PI * 2f);
        }
        if(goalRotation < 0){
            goalRotation += (float)Math.PI * 2f;
        }
    }

    @Override
    public Cell[] getTerritory() {
        return new Cell[]{
                new Cell((int)(location.x + 0.5f), (int)(location.y + 0.5f))
        };
    }

    @Override
    public int getMinDamage() {
        return minDamage;
    }
}
