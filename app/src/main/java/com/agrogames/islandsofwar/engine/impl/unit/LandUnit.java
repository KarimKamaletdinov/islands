package com.agrogames.islandsofwar.engine.impl.unit;

import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.game.GameObject;
import com.agrogames.islandsofwar.engine.abs.game.GameObjectProvider;
import com.agrogames.islandsofwar.engine.abs.game.GameObjectType;

import java.util.UUID;

class LandUnit extends Unit {
    public LandUnit(UUID id, GameObjectType type, Point location, int health, int speed) {
        super(id, type, location, health, speed);
    }

    @Override
    public void update(GameObjectProvider provider) {
        move(provider.getAll());
    }

    private void move(GameObject[] all){
        if(this.goal == null) return;
        if(new Cell(location).equals(goal)) { goal = null; return; }
        Point next = next();
        Cell nextCell = new Cell(next);
        if(isTaken(nextCell, all)) return;
        location = next;
    }

    private boolean isTaken(Cell cell, GameObject[] all){
        for (GameObject object : all){
            if(object != this){
                Cell[] territory = object.GetTerritory();
                for (Cell c : territory){
                    if(cell.equals(c)){
                        return false;
                    }
                }
            }
        }
        return false;
    }

    private Point next(){
        float lx = goal.x - location.x;
        float ly = goal.y - location.y;
        float l = (float) Math.sqrt((double)lx * (double)lx + (double)ly * (double)ly);
        float rel = l / speed;
        float dx = lx / rel;
        float dy = ly / rel;
        if(dx > lx) dx = lx;
        if(dy > ly) dy = ly;
        return new Point(location.x + dx, location.y + dy);
    }

    @Override
    public Cell[] GetTerritory(int tick) {
        return new Cell[]{
                new Cell(location)
        };
    }
}
