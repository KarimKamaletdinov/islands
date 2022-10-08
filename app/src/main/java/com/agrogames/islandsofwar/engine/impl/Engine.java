package com.agrogames.islandsofwar.engine.impl;

import com.agrogames.islandsofwar.engine.abs.game.Bullet;
import com.agrogames.islandsofwar.engine.abs.game.GameObject;
import com.agrogames.islandsofwar.engine.abs.map.MapObject;
import com.agrogames.islandsofwar.engine.impl.game.BulletAdder;
import com.agrogames.islandsofwar.engine.impl.game.GameObjectProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Engine implements com.agrogames.islandsofwar.engine.abs.Engine {
    private final List<GameObject> protectors = new ArrayList<>();
    private final List<GameObject> attackers = new ArrayList<>();
    private final List<Bullet> protectorsBullets = new ArrayList<>();
    private final List<Bullet> attackersBullets = new ArrayList<>();
    private final MapObject[] mapObjects;

    public Engine(GameObject[] protectors, GameObject[] attackers, MapObject[] mapObjects){
        this.protectors.addAll(Arrays.asList(protectors));
        this.attackers.addAll(Arrays.asList(attackers));
        this.mapObjects = mapObjects;
    }

    public void update(float deltaTime) {
        updateObjects(deltaTime);
        deleteKilled();
    }

    private void updateObjects(float deltaTime) {
        GameObject[] protectorsCopy = protectors.toArray(new GameObject[0]);
        GameObject[] attackersCopy = attackers.toArray(new GameObject[0]);
        Bullet[] protectorsBulletsCopy = protectorsBullets.toArray(new Bullet[0]);
        Bullet[] attackersBulletsCopy = attackersBullets.toArray(new Bullet[0]);

        GameObjectProvider provider1 = new GameObjectProvider(protectorsCopy, attackersCopy, mapObjects);
        BulletAdder bulletAdder1 = new BulletAdder();
        for (GameObject gameObject : protectorsCopy) {
            gameObject.update(provider1, bulletAdder1, deltaTime);
        }
        for (Bullet bullet : protectorsBulletsCopy) {
            bullet.update(provider1, bulletAdder1, deltaTime);
        }
        protectorsBullets.addAll(bulletAdder1.getBullets());

        BulletAdder bulletAdder2 = new BulletAdder();
        GameObjectProvider provider2 = new GameObjectProvider(attackersCopy, protectorsCopy, mapObjects);
        for (GameObject gameObject : attackersCopy) {
            gameObject.update(provider2, bulletAdder2, deltaTime);
        }
        for (Bullet bullet : attackersBulletsCopy) {
            bullet.update(provider2, bulletAdder2, deltaTime);
        }
        attackersBullets.addAll(bulletAdder2.getBullets());
    }

    private void deleteKilled() {
        for (GameObject gameObject : protectors.toArray(new GameObject[0])) {
            if(gameObject.getHealth().current <= 0){
                protectors.remove(gameObject);
            }
        }
        for (GameObject gameObject : attackers.toArray(new GameObject[0])) {
            if(gameObject.getHealth().current <= 0){
                attackers.remove(gameObject);
            }
        }

        for (Bullet bullet : protectorsBullets.toArray(new Bullet[0])) {
            if(bullet.getHealth().current <= 0){
                protectorsBullets.remove(bullet);
            }
        }
        for (Bullet bullet : attackersBullets.toArray(new Bullet[0])) {
            if(bullet.getHealth().current <= 0){
                attackersBullets.remove(bullet);
            }
        }
    }

    public GameObject[] getProtectors(){
        return protectors.toArray(new GameObject[0]);
    }

    public GameObject[] getAttackers(){
        return attackers.toArray(new GameObject[0]);
    }

    public Bullet[] getProtectorsBullets(){
        return protectorsBullets.toArray(new Bullet[0]);
    }

    public Bullet[] getAttackersBullets(){
        return attackersBullets.toArray(new Bullet[0]);
    }
}
