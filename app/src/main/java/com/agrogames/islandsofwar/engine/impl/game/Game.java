package com.agrogames.islandsofwar.engine.impl.game;

import com.agrogames.islandsofwar.engine.abs.game.GameObject;
import com.agrogames.islandsofwar.engine.abs.map.FutureMap;
import com.agrogames.islandsofwar.engine.abs.map.MapObject;
import com.agrogames.islandsofwar.engine.impl.map.FutureMapFactory;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final List<GameObject> army1 = new ArrayList<>();
    private final List<GameObject> army2 = new ArrayList<>();

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
        GameObject[] a1 = army1.toArray(new GameObject[0]);
        GameObject[] a2 = army2.toArray(new GameObject[0]);
        FutureMap futureMap = FutureMapFactory.Create(100, 100, getMap());

        GameObjectProvider provider1 = new GameObjectProvider(a1, a2);
        for (GameObject gameObject : a1) {
            gameObject.update(provider1);
        }

        GameObjectProvider provider2 = new GameObjectProvider(a2, a1);
        for (GameObject gameObject : a2) {
            gameObject.update(provider2);
        }
    }

    private void deleteKilled() {
        for (GameObject gameObject : army1.toArray(new GameObject[0])) {
            if(gameObject.getHealth().current <= 0){
                army1.remove(gameObject);
            }
        }
        for (GameObject gameObject : army2.toArray(new GameObject[0])) {
            if(gameObject.getHealth().current <= 0){
                army2.remove(gameObject);
            }
        }
    }

    private MapObject[] getMap(){
        List<MapObject> result = new ArrayList<>();
        result.addAll(army1);
        result.addAll(army2);
        return result.toArray(new MapObject[0]);
    }
}
