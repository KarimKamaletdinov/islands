package com.agrogames.islandsofwar.engine.impl.unit;

import android.os.Build;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import com.agrogames.islandsofwar.common.M;
import com.agrogames.islandsofwar.engine.abs.graphics.GraphicsAdder;
import com.agrogames.islandsofwar.engine.abs.bullet.BulletAdder;
import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.map.MapProvider;
import com.agrogames.islandsofwar.engine.abs.movable.MovableObject;
import com.agrogames.islandsofwar.engine.abs.transport.Transport;
import com.agrogames.islandsofwar.engine.abs.transport.TransportUnit;
import com.agrogames.islandsofwar.engine.abs.unit.IUnit;
import com.agrogames.islandsofwar.engine.abs.unit.IUnitAdder;
import com.agrogames.islandsofwar.factories.Factory;
import com.agrogames.islandsofwar.engine.abs.weapon.IWeapon;
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

    public BigShip(String texture, Cell location, IWeapon[] weapons, int health, float speed, float rotationSpeed, TransportUnit[] units, int minDamage) {
        super(texture, location, weapons, health, speed, rotationSpeed);
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
            int cdx = gx - MapParams.Width / 2;
            int cdy = gy - MapParams.Height / 2;
            if(M.module(cdx) > M.module(cdy)){
                if(cdx > 0){
                    gx = MapParams.Width;
                } else {
                    gx = 1;
                }
            } else {
                if(cdy > 0){
                    gy = MapParams.Height;
                } else {
                    gy = 1;
                }
            }
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

    @Override
    public Cell[] getRoute() {
        return route.toArray(new Cell[0]);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void update(MapProvider provider, BulletAdder bulletAdder, IUnitAdder unitAdder, GraphicsAdder graphicsAdder, float deltaTime) {
        for (IWeapon weapon: getWeapons()){
            weapon.update(provider, bulletAdder, unitAdder, graphicsAdder, deltaTime);
        }

        for (Object o : toAdd.toArray()){
            Cell l = new Cell(location);
            Cell c = new Cell(l.x + 1, l.y + 1);
            if(Arrays.stream(provider.getAll()).noneMatch(
                    x -> Arrays.asList(x.getTerritory()).contains(c) && !(x instanceof Water))){
                Pair<TransportUnit, Cell> u = (Pair<TransportUnit, Cell>)o;
                IUnit lc = Factory.get("landing_craft", l.x + 1, l.y + 1);
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

        if(!route.lastElement().equals(new Cell(location))){
            rotate(deltaTime);
        }
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

    @Override
    public Cell[] getTerritory() {
        Cell l = new Cell(location);
        float r = getRotation();
        float g45 = (float) Math.PI / 4.0f;
        if(r < g45 || r > g45 * 3 && r < g45 * 5 || r > g45 * 7){
            return new Cell[]{
                    l,
                    new Cell(l.x + 2, l.y),
                    new Cell(l.x + 1, l.y),
                    new Cell(l.x - 1, l.y),
                    new Cell(l.x - 2, l.y),
            };
        }
        return new Cell[]{
                l,
                new Cell(l.x, l.y + 2),
                new Cell(l.x, l.y + 1),
                new Cell(l.x, l.y - 1),
                new Cell(l.x, l.y - 2),
        };
    }
}
