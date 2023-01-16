package com.agrogames.islandsofwar.engine.impl;

import com.agrogames.islandsofwar.engine.abs.Engine;
import com.agrogames.islandsofwar.engine.abs.unit.IUnit;
import com.agrogames.islandsofwar.islands.abs.Island;
import com.agrogames.islandsofwar.islands.abs.IslandProvider;
import com.agrogames.islandsofwar.sounds.abs.SoundPlayer;

public class EngineFactory {
    private final IslandProvider islandProvider;

    public EngineFactory(IslandProvider islandProvider) {
        this.islandProvider = islandProvider;
    }

    public Engine create(int islandId, SoundPlayer soundPlayer){
        Island attacked = islandProvider.getAttackableById(islandId);
        return new com.agrogames.islandsofwar.engine.impl.Engine(
                attacked.owners,
                new IUnit[]{attacked.attacker},
                attacked.map.getMap(),
                soundPlayer);
    }
}
