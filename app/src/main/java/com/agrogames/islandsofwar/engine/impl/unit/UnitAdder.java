package com.agrogames.islandsofwar.engine.impl.unit;

import com.agrogames.islandsofwar.engine.abs.unit.IUnit;
import com.agrogames.islandsofwar.engine.abs.unit.IUnitAdder;

import java.util.ArrayList;
import java.util.List;

public class UnitAdder implements IUnitAdder {
    private final List<IUnit> units = new ArrayList<>();
    @Override
    public void addUnit(IUnit unit) {
        units.add(unit);
    }

    public List<IUnit> getUnits(){
        return units;
    }
}

