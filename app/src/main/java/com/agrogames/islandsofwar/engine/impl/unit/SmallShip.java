package com.agrogames.islandsofwar.engine.impl.unit;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.agrogames.islandsofwar.common.M;
import com.agrogames.islandsofwar.engine.abs.bullet.BulletAdder;
import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.map.MapObject;
import com.agrogames.islandsofwar.engine.abs.map.MapProvider;
import com.agrogames.islandsofwar.engine.abs.movable.MovableObject;
import com.agrogames.islandsofwar.engine.abs.transport.TransportUnit;
import com.agrogames.islandsofwar.engine.abs.unit.UnitAdder;
import com.agrogames.islandsofwar.factories.UnitFactory;
import com.agrogames.islandsofwar.types.UnitType;
import com.agrogames.islandsofwar.engine.abs.weapon.Weapon;
import com.agrogames.islandsofwar.engine.impl.navigator.Navigator;
import com.agrogames.islandsofwar.map.abs.MapParams;
import com.agrogames.islandsofwar.map.impl.Water;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class SmallShip extends Unit{
    private final Stack<Cell> route = new Stack<>();
    private Cell goal;
    private boolean isMoving;
    public TransportUnit unit;

    public SmallShip(UnitType type, Cell location, Weapon[] weapons, int health, float speed, float rotationSpeed) {
        super(type, location, weapons, health, speed, rotationSpeed);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void update(MapProvider provider, BulletAdder bulletAdder, UnitAdder unitAdder, float deltaTime) {
        if(goal != null){
            buildRoute(provider.getAll(), goal);
            goal = null;
        }
        rotate(deltaTime);
        move(provider.getAll(), unitAdder, deltaTime);
        for (Weapon weapon: getWeapons()){
            weapon.update(provider, bulletAdder, unitAdder, deltaTime);
        }
    }

    private void buildRoute(MapObject[] all, Cell goal){
        List<Cell> map = new ArrayList<>();
        for (MapObject object: all){
            if(!(object instanceof SmallShip)){
                for (Cell c: object.getTerritory()){
                    map.remove(c);
                }
            }
        }
        map.remove(goal);
        Cell[] r = new Cell[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            r = Navigator.buildRoute(location, goal, map.toArray(new Cell[0]));
        }
        route.clear();
        for (int i = r.length - 1; i >= 0; i--) {
            Cell c = r[i];
            route.push(c);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void move(MapObject[] all, UnitAdder adder, float deltaTime){
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
            stop(nextCell, all, adder);
            return;
        }
        if(isTaken(furtherCell, all)) {
            stop(furtherCell, all, adder);
            return;
        }
        location = next;
    }

    private void stop(Cell taken, MapObject[] all, UnitAdder adder){
        boolean isTakingMoving = false;
        boolean stoppedOnUnit = false;
        for (MapObject object: all){
            for (Cell cell: object.getTerritory()){
                if(cell.equals(taken)){
                    if(object != this && object instanceof SmallShip){
                        isTakingMoving = object.isMoving();
                    }
                    stoppedOnUnit = true;
                }
            }
        }
        if(adder != null && !stoppedOnUnit){
            land(adder, taken);
        }
        if (isTakingMoving){
            isMoving = false;
        } else {
            if(!route.isEmpty()) buildRoute(all, route.firstElement());
        }
    }

    private void land(UnitAdder adder, Cell cell){
        if(unit == null) return;
        com.agrogames.islandsofwar.engine.abs.unit.Unit u = null;
        switch (unit.type){
            case Tank:
                u = UnitFactory.Tank(cell.x, cell.y);
                break;
            case RocketLauncher:
                u = UnitFactory.RocketLauncher(cell.x, cell.y);
                break;
        }
        if(u == null) return;
        if(!route.isEmpty() && !cell.equals(route.firstElement())){
            ((MovableObject)u).setGoal(route.firstElement());
        }
        adder.addUnit(u);
        unit = null;
        health.current = 0;
        timeSinceDestroyed = 100;
    }

    private boolean isTaken(Cell cell, MapObject[] all){
        List<Cell> map = new ArrayList<>();
        for (int x = 0; x < MapParams.Width; x++){
            for (int y = 0; y < MapParams.Height; y++){
                map.add(new Cell(x, y));
            }
        }
        for (MapObject object: all){
            if(object instanceof Water){
                for (Cell c: object.getTerritory()){
                    map.remove(c);
                }
            } else if (object != this){
                Collections.addAll(map, object.getTerritory());
            }
        }

        return map.contains(cell);
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
    public int getHeight() {
        return 1;
    }

    @Override
    public void setGoal(Cell goal) {
        this.goal = goal;
    }
}
