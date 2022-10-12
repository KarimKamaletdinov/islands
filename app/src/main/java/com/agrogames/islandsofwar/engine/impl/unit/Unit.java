package com.agrogames.islandsofwar.engine.impl.unit;

import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.unit.UnitType;
import com.agrogames.islandsofwar.engine.abs.movable.MovableObject;
import com.agrogames.islandsofwar.engine.abs.weapon.Weapon;
import com.agrogames.islandsofwar.engine.abs.gamevalue.IntValue;

abstract class Unit implements com.agrogames.islandsofwar.engine.abs.unit.Unit, MovableObject {
    private final UnitType type;
    protected final IntValue health;
    protected final float speed;
    protected Point location;
    protected float rotation;
    private final Weapon[] weapons;

    protected Unit( UnitType type, Cell location, Weapon[] weapons, int health, float speed) {
        this.type = type;
        this.health = new IntValue(health);
        this.weapons = weapons;
        this.location = new Point(location);
        this.speed = speed;

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
        health.current -= lost;
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
    protected void rotate(Cell goal) {
        Point g = new Point(goal);
        rotation = (float) Math.atan(
                ((double) g.y - (double) location.y) /
                ((double) g.x - (double) location.x));
        if (g.x  < location.x){
            rotation += Math.PI;
        }
    }

    @Override
    public Cell[] GetTerritory() {
        return new Cell[]{
                new Cell((int)(location.x + 0.5f), (int)(location.y + 0.5f))
        };
    }
}
