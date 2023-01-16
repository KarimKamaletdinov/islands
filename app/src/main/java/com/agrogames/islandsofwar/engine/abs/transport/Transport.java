package com.agrogames.islandsofwar.engine.abs.transport;

import com.agrogames.islandsofwar.engine.abs.common.Cell;

public interface Transport {
    TransportUnit[] getUnits();
    void spawn(TransportUnit unit, Cell goal);
}
