package com.agrogames.islandsofwar.engine.abs.game;

import com.agrogames.islandsofwar.engine.abs.gamevalue.IntValue;
import com.agrogames.islandsofwar.engine.abs.map.FutureMap;
import com.agrogames.islandsofwar.engine.abs.map.MapObject;

import java.util.UUID;

public interface GameObject extends MapObject {
    UUID getId();
    GameObjectType getType();
    IntValue getHealth();
    void loseHealth(int lost);
    void update(GameObjectProvider provider);
}
