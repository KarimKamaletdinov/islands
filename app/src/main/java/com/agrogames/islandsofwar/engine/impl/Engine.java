package com.agrogames.islandsofwar.engine.impl;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.agrogames.islandsofwar.engine.abs.GameState;
import com.agrogames.islandsofwar.engine.abs.another.AnotherObject;
import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.impl.unit.Plane;
import com.agrogames.islandsofwar.factories.AnotherObjectFactory;
import com.agrogames.islandsofwar.engine.abs.bullet.Bullet;
import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObject;
import com.agrogames.islandsofwar.engine.abs.unit.Unit;
import com.agrogames.islandsofwar.engine.abs.map.MapObject;
import com.agrogames.islandsofwar.engine.impl.another.AnotherAdder;
import com.agrogames.islandsofwar.engine.impl.bullet.BulletAdder;
import com.agrogames.islandsofwar.engine.impl.map.MapProvider;
import com.agrogames.islandsofwar.engine.impl.unit.LandUnit;
import com.agrogames.islandsofwar.engine.impl.unit.UnitAdder;
import com.agrogames.islandsofwar.map.impl.Water;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Engine implements com.agrogames.islandsofwar.engine.abs.Engine {
    private final List<Unit> protectors = new ArrayList<>();
    private final List<Unit> attackers = new ArrayList<>();
    private final List<Unit> destroyed = new ArrayList<>();
    private final List<Bullet> protectorsBullets = new ArrayList<>();
    private final List<Bullet> attackersBullets = new ArrayList<>();
    private final List<AnotherObject> otherObjects = new ArrayList<>();
    private final MapObject[] mapObjects;
    private GameState state = GameState.Game;

    public Engine(Unit[] protectors, Unit[] attackers, MapObject[] mapObjects){
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
        Unit[] protectorsCopy = protectors.toArray(new Unit[0]);
        Unit[] attackersCopy = attackers.toArray(new Unit[0]);
        Bullet[] protectorsBulletsCopy = protectorsBullets.toArray(new Bullet[0]);
        Bullet[] attackersBulletsCopy = attackersBullets.toArray(new Bullet[0]);

        AnotherAdder anotherAdder = new AnotherAdder();
        MapProvider provider1 = new MapProvider(protectorsCopy, attackersCopy, mapObjects);
        BulletAdder bulletAdder1 = new BulletAdder();
        UnitAdder unitAdder1 = new UnitAdder();
        for (Unit unit : protectorsCopy) {
            try{
                unit.update(provider1, bulletAdder1, unitAdder1, anotherAdder, deltaTime);
            }catch (Exception e){
                Log.e("IOW", "An exception has occurred during Update", e);
            }
        }
        for (Bullet bullet : protectorsBulletsCopy) {
            try{
                bullet.update(provider1, bulletAdder1, unitAdder1, anotherAdder, deltaTime);
            }catch (Exception e){
                Log.e("IOW", "An exception has occurred during Update", e);
            }
        }
        for(Unit unit : destroyed.toArray(new Unit[0])){
            unit.addTsd(deltaTime);
        }
        for (AnotherObject another : otherObjects.toArray(new AnotherObject[0])){
            another.update(provider1, bulletAdder1, unitAdder1, anotherAdder, deltaTime);
        }
        protectorsBullets.addAll(bulletAdder1.getBullets());
        protectors.addAll(unitAdder1.getUnits());

        BulletAdder bulletAdder2 = new BulletAdder();
        MapProvider provider2 = new MapProvider(attackersCopy, protectorsCopy, mapObjects);
        UnitAdder unitAdder2 = new UnitAdder();
        for (Unit unit : attackersCopy) {
            try{
                unit.update(provider2, bulletAdder2, unitAdder2, anotherAdder, deltaTime);
            }catch (Exception e){
                Log.e("IOW", "An exception has occurred during Update", e);
            }
        }
        for (Bullet bullet : attackersBulletsCopy) {
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
        for (Unit unit : protectors.toArray(new Unit[0])) {
            if(unit.getHealth().current <= 0){
                if(unit instanceof LandUnit){
                    protectors.remove(unit);
                    otherObjects.add(AnotherObjectFactory.bang(unit));
                } else {
                    if(!destroyed.contains(unit)) destroyed.add(unit);
                }
            }
        }
        for (Unit unit : attackers.toArray(new Unit[0])) {
            if(unit.getHealth().current <= 0){
                if(unit instanceof LandUnit){
                    attackers.remove(unit);
                    otherObjects.add(AnotherObjectFactory.bang(unit));
                } else {
                    if(!destroyed.contains(unit)) destroyed.add(unit);
                }
            }
        }
        for(Unit unit : destroyed.toArray(new Unit[0])){
            if(unit.timeSinceDestroyed() >= 3.5f){
                destroyed.remove(unit);
                attackers.remove(unit);
                if(Arrays.stream(mapObjects).noneMatch(c -> c instanceof Water && Arrays.asList(c.getTerritory()).contains(new Cell(unit.getLocation())))){
                    otherObjects.add(AnotherObjectFactory.bigBang(unit));
                }
                for (Cell cell : unit.getTerritory()){
                    for(Unit u : protectors.stream().filter(c -> Arrays.asList(c.getTerritory()).contains(cell)).toArray(Unit[]::new)){
                        u.loseHealth(15);
                    }
                    for(Unit u : attackers.stream().filter(c -> Arrays.asList(c.getTerritory()).contains(cell)).toArray(Unit[]::new)){
                        u.loseHealth(15);
                    }
                }
            }
        }
        for(AnotherObject anotherObject : otherObjects.toArray(new AnotherObject[0])){
            if(anotherObject.shouldBeRemoved()){
                otherObjects.remove(anotherObject);
            }
        }

        for (Bullet bullet : protectorsBullets.toArray(new Bullet[0])) {
            if(bullet.hasStopped()){
                protectorsBullets.remove(bullet);
            }
        }
        for (Bullet bullet : attackersBullets.toArray(new Bullet[0])) {
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

    public Unit[] getProtectors(){
        return protectors.toArray(new Unit[0]);
    }

    public Unit[] getAttackers(){
        return attackers.toArray(new Unit[0]);
    }

    @Override
    @RequiresApi(Build.VERSION_CODES.N)
    public RenderableObject[] getOther() {
        List<RenderableObject> objects = new ArrayList<>();
        objects.addAll(destroyed);
        objects.addAll(otherObjects);
        return objects.toArray(new RenderableObject[0]);
    }

    public Bullet[] getProtectorsBullets(){
        return protectorsBullets.toArray(new Bullet[0]);
    }

    public Bullet[] getAttackersBullets(){
        return attackersBullets.toArray(new Bullet[0]);
    }

    @Override
    public GameState getState() {
        return state;
    }

    @Override
    public void addPlane(Unit plane) {
        attackers.add(plane);
    }
}
