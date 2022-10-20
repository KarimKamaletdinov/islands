package com.agrogames.islandsofwar.engine.impl.unit;

import android.os.Build;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import com.agrogames.islandsofwar.engine.abs.bullet.BulletAdder;
import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.map.MapProvider;
import com.agrogames.islandsofwar.engine.abs.movable.MovableObject;
import com.agrogames.islandsofwar.engine.abs.transport.Transport;
import com.agrogames.islandsofwar.engine.abs.transport.TransportUnit;
import com.agrogames.islandsofwar.engine.abs.unit.UnitAdder;
import com.agrogames.islandsofwar.engine.abs.unit.UnitType;
import com.agrogames.islandsofwar.engine.abs.weapon.Weapon;
import com.agrogames.islandsofwar.engine.impl.navigator.BigShipNavigator;
import com.agrogames.islandsofwar.map.abs.MapParams;
import com.agrogames.islandsofwar.map.impl.Water;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class BigShip extends Unit implements Transport {
    private final Stack<Cell> route = new Stack<>();
    private final List<TransportUnit> units = new ArrayList<>();
    private final List<Pair<TransportUnit, Cell>> toAdd = new ArrayList<>();

    public BigShip(UnitType type, Cell location, Weapon[] weapons, int health, float speed, float rotationSpeed, TransportUnit[] units, int minDamage) {
        super(type, location, weapons, health, speed, rotationSpeed);
        this.minDamage = minDamage;
        Collections.addAll(this.units, units);
    }

    @Override
    public boolean isMoving() {
        return false;
    }

    @Override
    public int getHeight() {
        return 1;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void setGoal(Cell goal) {
        int gx = goal.x;
        int gy = goal.y;
        if(goal.x != 1 && goal.x != MapParams.Width &&
                goal.y != 1 && goal.y != MapParams.Height){
            if(goal.x < MapParams.Width / 2)
                gx = 1;
            if(goal.x > MapParams.Width / 2)
                gx = MapParams.Width;
            if(goal.y < MapParams.Height / 2)
                gy = 1;
            if(goal.y > MapParams.Height / 2)
                gy = MapParams.Height;
        }
        if(goal.x < 1)
            gx = 1;
        if(goal.y < 1)
            gy = 1;
        if(goal.x > MapParams.Width)
            gx = MapParams.Width;
        if(goal.y > MapParams.Height)
            gy = MapParams.Height;
        List<Cell> r = BigShipNavigator.buildRoute(new Cell(location), new Cell(gx, gy));
        route.clear();
        for (int i = r.size() - 1; i >= 0; i--) {
            Cell c = r.get(i);
            route.push(c);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void update(MapProvider provider, BulletAdder bulletAdder, UnitAdder unitAdder, float deltaTime) {
        for (Weapon weapon: getWeapons()){
            weapon.update(provider, bulletAdder, unitAdder, deltaTime);
        }

        for (Object o : toAdd.toArray()){
            Cell l = new Cell(location);
            Cell c = new Cell(l.x + 1, l.y + 1);
            if(Arrays.stream(provider.getAll()).noneMatch(
                    x -> Arrays.asList(x.getTerritory()).contains(c) && !(x instanceof Water))){
                Pair<TransportUnit, Cell> u = (Pair<TransportUnit, Cell>)o;
                com.agrogames.islandsofwar.engine.abs.unit.Unit lc = UnitFactory.LandingCraft(l.x + 1, l.y + 1);
                ((SmallShip)lc).unit = u.first;
                ((MovableObject)lc).setGoal(u.second);
                unitAdder.addUnit(lc);
                toAdd.remove(o);
            }
            break;
        }

        if(route.isEmpty()) return;
        if(new Point(route.lastElement()).equals(location))
            route.pop();
        if(route.isEmpty()) return;

        Point g = new Point(route.lastElement());
        if(g.x > location.x){
            location.x += speed * deltaTime;
            if(location.x > g.x) location.x = g.x;
            goalRotation = 0;
        } else if(g.x < location.x){
            location.x -= speed * deltaTime;
            if(location.x < g.x) location.x = g.x;
            goalRotation = -(float) Math.PI;
        } else if(g.y > location.y){
            location.y += speed * deltaTime;
            if(location.y > g.y) location.y = g.y;
            goalRotation = (float) Math.PI / 2f;
        } else if(g.y < location.y){
            location.y -= speed * deltaTime;
            if(location.y < g.y) location.y = g.y;
            goalRotation = -(float) Math.PI / 2f;
        }

        rotate(deltaTime);
    }

    @Override
    public TransportUnit[] getUnits() {
        return units.toArray(new TransportUnit[0]);
    }

    @Override
    public void spawn(TransportUnit unit, Cell goal) {
        if(units.remove(unit)){
            toAdd.add(new Pair<>(unit, goal));
        }
    }
}
