package com.agrogames.islandsofwar.engine.impl;

import android.util.Log;

import com.agrogames.islandsofwar.engine.abs.bullet.Bullet;
import com.agrogames.islandsofwar.engine.abs.unit.Unit;
import com.agrogames.islandsofwar.engine.abs.map.MapObject;
import com.agrogames.islandsofwar.engine.impl.bullet.BulletAdder;
import com.agrogames.islandsofwar.engine.impl.map.MapProvider;
import com.agrogames.islandsofwar.engine.impl.unit.UnitAdder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Engine implements com.agrogames.islandsofwar.engine.abs.Engine {
    private final List<Unit> protectors = new ArrayList<>();
    private final List<Unit> attackers = new ArrayList<>();
    private final List<Bullet> protectorsBullets = new ArrayList<>();
    private final List<Bullet> attackersBullets = new ArrayList<>();
    private final MapObject[] mapObjects;

    public Engine(Unit[] protectors, Unit[] attackers, MapObject[] mapObjects){
        this.protectors.addAll(Arrays.asList(protectors));
        this.attackers.addAll(Arrays.asList(attackers));
        this.mapObjects = mapObjects;
    }

    public void update(float deltaTime) {
        updateObjects(deltaTime);
        deleteKilled();
    }

    private void updateObjects(float deltaTime) {
        Unit[] protectorsCopy = protectors.toArray(new Unit[0]);
        Unit[] attackersCopy = attackers.toArray(new Unit[0]);
        Bullet[] protectorsBulletsCopy = protectorsBullets.toArray(new Bullet[0]);
        Bullet[] attackersBulletsCopy = attackersBullets.toArray(new Bullet[0]);

        MapProvider provider1 = new MapProvider(protectorsCopy, attackersCopy, mapObjects);
        BulletAdder bulletAdder1 = new BulletAdder();
        UnitAdder unitAdder1 = new UnitAdder();
        for (Unit unit : protectorsCopy) {
            try{
                unit.update(provider1, bulletAdder1, unitAdder1, deltaTime);
            }catch (Exception e){
                Log.e("IOW", "An exception has occurred during Update", e);
            }
        }
        for (Bullet bullet : protectorsBulletsCopy) {
            try{
                bullet.update(provider1, bulletAdder1, unitAdder1, deltaTime);
            }catch (Exception e){
                Log.e("IOW", "An exception has occurred during Update", e);
            }
        }
        protectorsBullets.addAll(bulletAdder1.getBullets());
        protectors.addAll(unitAdder1.getUnits());

        BulletAdder bulletAdder2 = new BulletAdder();
        MapProvider provider2 = new MapProvider(attackersCopy, protectorsCopy, mapObjects);
        UnitAdder unitAdder2 = new UnitAdder();
        for (Unit unit : attackersCopy) {
            try{
                unit.update(provider2, bulletAdder2, unitAdder2, deltaTime);
            }catch (Exception e){
                Log.e("IOW", "An exception has occurred during Update", e);
            }
        }
        for (Bullet bullet : attackersBulletsCopy) {
            try{
                bullet.update(provider2, bulletAdder2, unitAdder2, deltaTime);
            }catch (Exception e){
                Log.e("IOW", "An exception has occurred during Update", e);
            }
        }
        attackersBullets.addAll(bulletAdder2.getBullets());
        attackers.addAll(unitAdder2.getUnits());
    }

    private void deleteKilled() {
        for (Unit unit : protectors.toArray(new Unit[0])) {
            if(unit.getHealth().current <= 0){
                protectors.remove(unit);
            }
        }
        for (Unit unit : attackers.toArray(new Unit[0])) {
            if(unit.getHealth().current <= 0){
                attackers.remove(unit);
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

    public Unit[] getProtectors(){
        return protectors.toArray(new Unit[0]);
    }

    public Unit[] getAttackers(){
        return attackers.toArray(new Unit[0]);
    }

    public Bullet[] getProtectorsBullets(){
        return protectorsBullets.toArray(new Bullet[0]);
    }

    public Bullet[] getAttackersBullets(){
        return attackersBullets.toArray(new Bullet[0]);
    }
}
