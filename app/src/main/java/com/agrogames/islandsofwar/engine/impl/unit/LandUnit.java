package com.agrogames.islandsofwar.engine.impl.unit;

import android.os.Build;
import androidx.annotation.RequiresApi;

import com.agrogames.islandsofwar.common.M;
import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.bullet.BulletAdder;
import com.agrogames.islandsofwar.engine.abs.map.MapProvider;
import com.agrogames.islandsofwar.engine.abs.unit.UnitType;
import com.agrogames.islandsofwar.engine.abs.weapon.Weapon;
import com.agrogames.islandsofwar.engine.abs.map.MapObject;
import com.agrogames.islandsofwar.engine.abs.unit.Unit;
import com.agrogames.islandsofwar.engine.impl.navigator.Navigator;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

class LandUnit extends com.agrogames.islandsofwar.engine.impl.unit.Unit {
    private Unit goalUnit;
    private final Stack<Cell> route = new Stack<>();
    private Cell goal;
    private boolean isMoving;

    public LandUnit(UnitType type, Cell location, Weapon[] weapons, int health, float speed) {
        super(type, location, weapons, health, speed);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void update(MapProvider provider, BulletAdder bulletAdder, float deltaTime) {
        think(provider);
        rotate(deltaTime);
        move(provider.getAll(), deltaTime);
        for (Weapon weapon: getWeapons()){
            weapon.update(provider, bulletAdder,  deltaTime);
        }
    }

    private void buildRoute(MapObject[] all, Cell goal){
        List<Cell> map = new ArrayList<>();
        for (MapObject object: all){
            if(object != this){
                Collections.addAll(map, object.GetTerritory());
            }
        }
        map.remove(goal);
        Cell[] r = new Cell[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            r = Navigator.buildRoute(location, goal, map.toArray(new Cell[0]));
        }
        for (int i = r.length - 1; i >= 0; i--) {
            Cell c = r[i];
            route.push(c);
        }
    }

    private void think(MapProvider provider){
        if(goal != null){
            buildRoute(provider.getAll(), goal);
            goal = null;
        }

        if(route.isEmpty()){
            if(goalUnit == null){
                Unit closest = null;
                Float distToClosest = null;
                for (Unit enemy: provider.getEnemies()){
                    float dist = getDist(location, enemy.getLocation());
                    if(distToClosest == null){
                        closest = enemy;
                        distToClosest = dist;
                    } else{
                        if(dist < distToClosest){
                            closest = enemy;
                            distToClosest = dist;
                        }
                    }
                }
                if(closest != null){
                    if(distToClosest > 5){
                        goalUnit = closest;
                        buildRoute(provider.getAll(), goalUnit.GetTerritory()[0]);
                        setRotation(new Cell(goalUnit.getLocation()));
                    }
                }
            } else {
                if(getDist(location, goalUnit.getLocation()) <= 5){
                    goalUnit = null;
                }
            }
        } else if(goalUnit != null) {
            if(getDist(location, goalUnit.getLocation()) <= 5){
                goalUnit = null;
                route.clear();
            }
        }
    }

    private float getDist(Point p1, Point p2) {
        float dx = p2.x - p1.x;
        float dy = p2.y - p1.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void move(MapObject[] all, float deltaTime){
        if(route.isEmpty()){
            isMoving = false;
            return;
        }
        if(new Cell(location).equals(route.lastElement())){
            route.pop();
        }
        if(route.isEmpty()){
            isMoving = false;
            return;
        }

        Cell g = route.lastElement();
        setRotation(g);
        Point next = next(deltaTime, g);
        Cell nextCell = new Cell(next);
        Point further = next(0.5f / speed, g);
        Cell furtherCell = new Cell(further);
        if(isTaken(nextCell, all)) {
            stop(nextCell, all);
            return;
        }
        if(isTaken(furtherCell, all)) {
            stop(furtherCell, all);
            return;
        }
        location = next;
    }

    private void stop(Cell taken, MapObject[] all){
        boolean isTakingMoving = false;
        for (MapObject object: all){
            if(object != this){
                for (Cell cell: object.GetTerritory()){
                    if(cell == taken){
                        isTakingMoving = object.isMoving();
                    }
                }
            }
        }
        if (isTakingMoving){
            isMoving = false;
        } else {
            if(!route.isEmpty()) buildRoute(all, route.firstElement());
        }
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

    private Point next(float deltaTime, Cell goal){
        Point g = new Point(goal);
        double lx = g.x - location.x;
        double ly = g.y - location.y;
        double l = Math.sqrt(lx * lx + ly * ly);
        double rel = l / (speed * deltaTime);
        double dx = lx / rel;
        double dy = ly / rel;
        if(M.module(dx) > M.module(lx)){
            dx = lx;
            dy = ly;
        }
        if(Double.isNaN(dx)){
            dx = 0;
        }
        if(Double.isNaN(dy)){
            dy = 0;
        }
        return new Point(location.x + (float)dx,  location.y + (float) dy);
    }

    @Override
    public boolean isMoving() {
        return isMoving;
    }

    @Override
    public void setGoal(Cell goal) {
        this.goal = goal;
    }
}
