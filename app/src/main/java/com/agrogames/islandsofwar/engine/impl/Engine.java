package com.agrogames.islandsofwar.engine.impl;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.agrogames.islandsofwar.engine.abs.GameState;
import com.agrogames.islandsofwar.engine.abs.another.IGraphicsObject;
import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.bullet.IBullet;
import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObject;
import com.agrogames.islandsofwar.engine.abs.unit.IUnit;
import com.agrogames.islandsofwar.engine.abs.map.MapObject;
import com.agrogames.islandsofwar.engine.impl.another.AnotherAdder;
import com.agrogames.islandsofwar.engine.impl.bullet.BulletAdder;
import com.agrogames.islandsofwar.engine.impl.map.MapProvider;
import com.agrogames.islandsofwar.engine.impl.unit.LandUnit;
import com.agrogames.islandsofwar.engine.impl.unit.UnitAdder;
import com.agrogames.islandsofwar.factories.Factory;
import com.agrogames.islandsofwar.map.impl.Water;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Engine implements com.agrogames.islandsofwar.engine.abs.Engine {
    private final List<IUnit> protectors = new ArrayList<>();
    private final List<IUnit> attackers = new ArrayList<>();
    private final List<IUnit> destroyed = new ArrayList<>();
    private final List<IBullet> protectorsBullets = new ArrayList<>();
    private final List<IBullet> attackersBullets = new ArrayList<>();
    private final List<IGraphicsObject> otherObjects = new ArrayList<>();
    private final MapObject[] mapObjects;
    private GameState state = GameState.Game;

    public Engine(IUnit[] protectors, IUnit[] attackers, MapObject[] mapObjects){
        this.protectors.addAll(Arrays.asList(protectors));
        this.attackers.addAll(Arrays.asList(attackers));
        this.mapObjects = mapObjects;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void update(float deltaTime) {
        updateObjects(deltaTime);
        deleteKilled();
        updateState();
    }

    private void updateObjects(float deltaTime) {
        IUnit[] protectorsCopy = protectors.toArray(new IUnit[0]);
        IUnit[] attackersCopy = attackers.toArray(new IUnit[0]);
        IBullet[] protectorsBulletsCopy = protectorsBullets.toArray(new IBullet[0]);
        IBullet[] attackersBulletsCopy = attackersBullets.toArray(new IBullet[0]);

        AnotherAdder anotherAdder = new AnotherAdder();
        MapProvider provider1 = new MapProvider(protectorsCopy, attackersCopy, mapObjects);
        BulletAdder bulletAdder1 = new BulletAdder();
        UnitAdder unitAdder1 = new UnitAdder();
        for (IUnit unit : protectorsCopy) {
            try{
                unit.update(provider1, bulletAdder1, unitAdder1, anotherAdder, deltaTime);
            }catch (Exception e){
                Log.e("IOW", "An exception has occurred during Update", e);
            }
        }
        for (IBullet bullet : protectorsBulletsCopy) {
            try{
                bullet.update(provider1, bulletAdder1, unitAdder1, anotherAdder, deltaTime);
            }catch (Exception e){
                Log.e("IOW", "An exception has occurred during Update", e);
            }
        }
        for(IUnit unit : destroyed.toArray(new IUnit[0])){
            unit.addTsd(deltaTime);
        }
        for (IGraphicsObject another : otherObjects.toArray(new IGraphicsObject[0])){
            another.update(provider1, bulletAdder1, unitAdder1, anotherAdder, deltaTime);
        }
        protectorsBullets.addAll(bulletAdder1.getBullets());
        protectors.addAll(unitAdder1.getUnits());

        BulletAdder bulletAdder2 = new BulletAdder();
        MapProvider provider2 = new MapProvider(attackersCopy, protectorsCopy, mapObjects);
        UnitAdder unitAdder2 = new UnitAdder();
        for (IUnit unit : attackersCopy) {
            try{
                unit.update(provider2, bulletAdder2, unitAdder2, anotherAdder, deltaTime);
            }catch (Exception e){
                Log.e("IOW", "An exception has occurred during Update", e);
            }
        }
        for (IBullet bullet : attackersBulletsCopy) {
            try{
                bullet.update(provider2, bulletAdder2, unitAdder2, anotherAdder, deltaTime);
            }catch (Exception e){
                Log.e("IOW", "An exception has occurred during Update", e);
            }
        }
        attackersBullets.addAll(bulletAdder2.getBullets());
        attackers.addAll(unitAdder2.getUnits());

        otherObjects.addAll(anotherAdder.getAnotherObjects());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void deleteKilled() {
        for (IUnit unit : protectors.toArray(new IUnit[0])) {
            if(unit.getHealth().current <= 0){
                if(unit instanceof LandUnit){
                    protectors.remove(unit);
                    otherObjects.add(Factory.getGraphics("bang", unit.getLocation(), unit.getRotation()));
                } else {
                    if(!destroyed.contains(unit)) destroyed.add(unit);
                }
            }
        }
        for (IUnit unit : attackers.toArray(new IUnit[0])) {
            if(unit.getHealth().current <= 0){
                if(unit instanceof LandUnit){
                    attackers.remove(unit);
                    otherObjects.add(Factory.getGraphics("bang", unit.getLocation(), unit.getRotation()));
                } else {
                    if(!destroyed.contains(unit)) destroyed.add(unit);
                }
            }
        }
        for(IUnit unit : destroyed.toArray(new IUnit[0])){
            if(unit.timeSinceDestroyed() >= 3.5f){
                destroyed.remove(unit);
                attackers.remove(unit);
                if(Arrays.stream(mapObjects).noneMatch(c -> c instanceof Water && Arrays.asList(c.getTerritory()).contains(new Cell(unit.getLocation())))){
                    otherObjects.add(Factory.getGraphics("big_bang", unit.getLocation(), unit.getRotation()));
                }
                for (Cell cell : unit.getTerritory()){
                    for(IUnit u : protectors.stream().filter(c -> Arrays.asList(c.getTerritory()).contains(cell)).toArray(IUnit[]::new)){
                        u.loseHealth(15);
                    }
                    for(IUnit u : attackers.stream().filter(c -> Arrays.asList(c.getTerritory()).contains(cell)).toArray(IUnit[]::new)){
                        u.loseHealth(15);
                    }
                }
            }
        }
        for(IGraphicsObject graphicsObject : otherObjects.toArray(new IGraphicsObject[0])){
            if(graphicsObject.shouldBeRemoved()){
                otherObjects.remove(graphicsObject);
            }
        }

        for (IBullet bullet : protectorsBullets.toArray(new IBullet[0])) {
            if(bullet.hasStopped()){
                protectorsBullets.remove(bullet);
            }
        }
        for (IBullet bullet : attackersBullets.toArray(new IBullet[0])) {
            if(bullet.hasStopped()){
                attackersBullets.remove(bullet);
            }
        }
    }

    private void updateState(){
        if(state != GameState.Game) return;
        if(attackers.size() == 0) state = GameState.Defeat;
        else if(protectors.size() == 0) state = GameState.Win;
    }

    public IUnit[] getProtectors(){
        return protectors.toArray(new IUnit[0]);
    }

    public IUnit[] getAttackers(){
        return attackers.toArray(new IUnit[0]);
    }

    @Override
    @RequiresApi(Build.VERSION_CODES.N)
    public RenderableObject[] getOther() {
        List<RenderableObject> objects = new ArrayList<>();
        objects.addAll(destroyed);
        objects.addAll(otherObjects);
        return objects.toArray(new RenderableObject[0]);
    }

    public IBullet[] getProtectorsBullets(){
        return protectorsBullets.toArray(new IBullet[0]);
    }

    public IBullet[] getAttackersBullets(){
        return attackersBullets.toArray(new IBullet[0]);
    }

    @Override
    public GameState getState() {
        return state;
    }

    @Override
    public void addPlane(IUnit plane) {
        attackers.add(plane);
    }
}
