package com.agrogames.islandsofwar.islands.abs;

import com.agrogames.islandsofwar.engine.abs.unit.IUnit;
import com.agrogames.islandsofwar.map.abs.Map;

public class Island {
    public final int id;
    public final Map map;
    public final IUnit[] owners;
    public final IUnit attacker;

    public Island(int id, Map map, IUnit[] owners, IUnit attacker) {
        this.id = id;
        this.map = map;
        this.owners = owners;
        this.attacker = attacker;
    }
}
