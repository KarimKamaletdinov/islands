package com.agrogames.islandsofwar.engine.impl.unit;

import com.agrogames.islandsofwar.engine.abs.unit.IUnit;
import com.agrogames.islandsofwar.engine.abs.unit.IUnitAdder;
import com.agrogames.islandsofwar.sounds.abs.SoundPlayer;

import java.util.ArrayList;
import java.util.List;

public class UnitAdder implements IUnitAdder {
    private final List<IUnit> units = new ArrayList<>();
    private final SoundPlayer soundPlayer;

    public UnitAdder(SoundPlayer soundPlayer) {
        this.soundPlayer = soundPlayer;
    }

    @Override
    public void addUnit(IUnit unit) {
        units.add(unit);
        soundPlayer.playSound("landing");
    }

    public List<IUnit> getUnits(){
        return units;
    }
}

