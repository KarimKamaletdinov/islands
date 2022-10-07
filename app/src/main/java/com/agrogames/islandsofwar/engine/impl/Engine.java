package com.agrogames.islandsofwar.engine.impl;

import com.agrogames.islandsofwar.engine.abs.game.GameObject;
import com.agrogames.islandsofwar.engine.impl.game.GameObjectProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Engine implements com.agrogames.islandsofwar.engine.abs.Engine {
    private final List<GameObject> protectors = new ArrayList<>();
    private final List<GameObject> attackers = new ArrayList<>();

    public Engine(GameObject[] protectors, GameObject[] attackers){
        this.protectors.addAll(Arrays.asList(protectors));
        this.attackers.addAll(Arrays.asList(attackers));
    }

    public void update(int ticksPassed){
        for (int i = 0; i < ticksPassed; i++){
            update();
        }
    }

    private void update() {
        updateObjects();
        deleteKilled();
    }

    private void updateObjects() {
        GameObject[] protectorsCopy = protectors.toArray(new GameObject[0]);
        GameObject[] attackersCopy = attackers.toArray(new GameObject[0]);

        GameObjectProvider provider1 = new GameObjectProvider(protectorsCopy, attackersCopy);
        for (GameObject gameObject : protectorsCopy) {
            gameObject.update(provider1);
        }

        GameObjectProvider provider2 = new GameObjectProvider(attackersCopy, protectorsCopy);
        for (GameObject gameObject : attackersCopy) {
            gameObject.update(provider2);
        }
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
    }

    public GameObject[] getProtectors(){
        return protectors.toArray(new GameObject[0]);
    }

    public GameObject[] getAttackers(){
        return attackers.toArray(new GameObject[0]);
    }
}
