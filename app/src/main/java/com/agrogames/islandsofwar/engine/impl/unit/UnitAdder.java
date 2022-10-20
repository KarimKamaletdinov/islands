package com.agrogames.islandsofwar.engine.impl.unit;

import java.util.ArrayList;
import java.util.List;

public class UnitAdder implements com.agrogames.islandsofwar.engine.abs.unit.UnitAdder {
    private final List<com.agrogames.islandsofwar.engine.abs.unit.Unit > units = new ArrayList<>();
    @Override
    public void addUnit(com.agrogames.islandsofwar.engine.abs.unit.Unit unit) {
        units.add(unit);
    }

    public List<com.agrogames.islandsofwar.engine.abs.unit.Unit> getUnits(){
        return units;
    }
}

