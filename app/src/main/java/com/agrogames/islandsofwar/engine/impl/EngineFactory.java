package com.agrogames.islandsofwar.engine.impl;

import com.agrogames.islandsofwar.engine.abs.Engine;
import com.agrogames.islandsofwar.engine.abs.unit.IUnit;
import com.agrogames.islandsofwar.factories.UnitFactory;
import com.agrogames.islandsofwar.islands.abs.Island;
import com.agrogames.islandsofwar.islands.abs.IslandProvider;
import com.agrogames.islandsofwar.islands.abs.User;
import com.agrogames.islandsofwar.islands.abs.UserProvider;
import com.agrogames.islandsofwar.sounds.abs.SoundPlayer;

public class EngineFactory {
    private final IslandProvider islandProvider;
    private final UserProvider userProvider;

    public EngineFactory(IslandProvider islandProvider, UserProvider userProvider) {
        this.islandProvider = islandProvider;
        this.userProvider = userProvider;
    }

    public Engine create(int islandId, SoundPlayer soundPlayer){
        Island attacked = islandProvider.getAttackableById(islandId);
        User user = userProvider.get();
        return new com.agrogames.islandsofwar.engine.impl.Engine(
                attacked.owners,
                new IUnit[]{UnitFactory.get(user.shipName, 1, 1, user.shipContent)},
                attacked.map.getMap(),
                soundPlayer);
    }
}
