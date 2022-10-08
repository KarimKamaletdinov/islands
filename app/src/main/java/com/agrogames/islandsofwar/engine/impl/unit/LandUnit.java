package com.agrogames.islandsofwar.engine.impl.unit;

import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.game.BulletAdder;
import com.agrogames.islandsofwar.engine.abs.game.GameObject;
import com.agrogames.islandsofwar.engine.abs.game.GameObjectProvider;
import com.agrogames.islandsofwar.engine.abs.game.GameObjectType;
import com.agrogames.islandsofwar.engine.abs.map.MapObject;

import java.util.UUID;

class LandUnit extends Unit {
    private float fromLastReload = 0;

    public LandUnit(UUID id, GameObjectType type, Point location, int health, float speed, float reload) {
        super(id, type, location, health, speed, reload);
    }

    @Override
    public void update(GameObjectProvider provider, BulletAdder bulletAdder, float deltaTime) {
        move(provider.getAll(), deltaTime);
        shoot(provider.getEnemies(), bulletAdder, deltaTime);
    }

    private void shoot(GameObject[] enemies, BulletAdder bulletAdder, float deltaTime){
        fromLastReload += deltaTime;
        if(fromLastReload < reload) return;
        fromLastReload = 0;

        for (GameObject enemy: enemies){
            Bullet bullet = BulletFactory.Create(this);
            bullet.setGoal(enemy.getLocation());
            bulletAdder.AddBullet(bullet);
        }
    }

    private void move(MapObject[] all, float deltaTime){
        if(this.goal == null) return;
        if(new Cell(location).equals(goal)) { goal = null; return; }

        Point next = next(deltaTime);
        Cell nextCell = new Cell(next);
        if(isTaken(nextCell, all)) return;

        Point further = next(0.5f / speed);
        Cell furtherCell = new Cell(further);
        if(isTaken(furtherCell, all))
            return;
        location = next;
    }

    private boolean isTaken(Cell cell, MapObject[] all){
        for (MapObject object : all){
            if(object != this){
                Cell[] territory = object.GetTerritory();
                for (Cell c : territory){
                    if(cell.equals(c)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Point next(float deltaTime){
        double lx = goal.x - location.x;
        double ly = goal.y - location.y;
        double l = Math.sqrt(lx * lx + ly * ly);
        double rel = l / (speed * deltaTime);
        double dx = lx / rel;
        double dy = ly / rel;
        //if(dx > lx) dx = lx;
        //if(dy > ly) dy = ly;
        return new Point(location.x + (float)dx,  location.y + (float) dy);
    }
}
