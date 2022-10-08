package com.agrogames.islandsofwar.engine.impl.unit;

import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.game.BulletAdder;
import com.agrogames.islandsofwar.engine.abs.game.GameObject;
import com.agrogames.islandsofwar.engine.abs.game.GameObjectProvider;
import com.agrogames.islandsofwar.engine.abs.game.GameObjectType;
import com.agrogames.islandsofwar.engine.abs.gamevalue.IntValue;

import java.util.UUID;

public class Bullet implements com.agrogames.islandsofwar.engine.abs.game.Bullet {
    private final UUID id;
    private final GameObjectType type;
    private Point location;
    private float rotation;
    private final float speed;
    private final int power;
    private final float longRange;
    private final IntValue health;
    private float flewDistance;

    public Bullet(GameObjectType type, Point location, int speed, int power, float longRange) {
        this.longRange = longRange;
        this.id = UUID.randomUUID();
        this.type = type;
        this.location = location;
        this.speed = speed;
        this.power = power;
        this.health = new IntValue(1);
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public GameObjectType getType() {
        return type;
    }

    @Override
    public IntValue getHealth() {
        return health;
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
    public void loseHealth(int lost) {
    }

    @Override
    public void update(GameObjectProvider provider, BulletAdder bulletAdder, float deltaTime) {
        move(deltaTime);
        GameObject enemy = enemyAt(new Cell(location), provider.getEnemies());
        if(enemy != null){
            enemy.loseHealth(power);
            health.current = 0;
        }
        if(flewDistance >= longRange){
            health.current = 0;
        }
    }

    private void move(float deltaTime){
        double dx = Math.cos(rotation) * (double) speed * (double) deltaTime;
        double dy = Math.sin(rotation) * (double) speed * (double) deltaTime;
        location = new Point(location.x + (float)dx,  location.y + (float) dy);
        flewDistance += speed * deltaTime;
    }

    private GameObject enemyAt(Cell cell, GameObject[] enemies){
        for (GameObject object : enemies){
            if(object != this){
                Cell[] territory = object.GetTerritory();
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
    public Cell[] GetTerritory() {
        return new Cell[0];
    }

    @Override
    public void setGoal(Point goal) {
        this.rotation = (float) Math.atan(
                ((double) goal.y - (double) location.y) /
                        ((double) goal.x - (double) location.x));
        if (goal.x  < location.x){
            rotation += Math.PI;
        }
    }
}
