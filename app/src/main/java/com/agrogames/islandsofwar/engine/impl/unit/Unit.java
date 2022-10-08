package com.agrogames.islandsofwar.engine.impl.unit;

import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.game.GameObject;
import com.agrogames.islandsofwar.engine.abs.game.GameObjectType;
import com.agrogames.islandsofwar.engine.abs.game.MovableObject;
import com.agrogames.islandsofwar.engine.abs.gamevalue.IntValue;

import java.util.UUID;

abstract class Unit implements GameObject, MovableObject {
    private final UUID id;
    private final GameObjectType type;
    protected final IntValue health;
    protected final float speed;
    protected final float reload;
    protected Point location;
    protected Cell goal = null;
    protected float rotation;

    protected Unit(UUID id, GameObjectType type, Point location, int health, float speed, float reload) {
        this.id = id;
        this.type = type;
        this.health = new IntValue(health);
        this.location = location;
        this.speed = speed;
        this.reload = reload;
    }

    @Override
    public GameObjectType getType() {
        return type;
    }

    @Override
    public UUID getId() {
        return id;
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

    @Override
    public float getRotation() {
        return rotation;
    }

    @Override
    public void setGoal(Cell goal) {
        this.goal = goal;
        this.rotation = (float) Math.atan(
                ((double) goal.y - (double) location.y) /
                ((double) goal.x - (double) location.x));
        if (goal.x  < location.x){
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
