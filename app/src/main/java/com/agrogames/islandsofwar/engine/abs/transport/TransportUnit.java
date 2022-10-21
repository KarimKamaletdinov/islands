package com.agrogames.islandsofwar.engine.abs.transport;

import com.agrogames.islandsofwar.types.UnitType;

import java.util.Objects;

public class TransportUnit {
    public final UnitType type;

    public TransportUnit(UnitType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransportUnit that = (TransportUnit) o;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
