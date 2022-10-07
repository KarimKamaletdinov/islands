package com.agrogames.islandsofwar.engine.impl.routebuilder;

import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.map.FutureMap;

import java.util.ArrayList;
import java.util.List;

public class RouteBuilder {
    public static Cell[] Build(FutureMap map, Unit unit){
        List<Cell> list = new ArrayList<>();
        list.add(new Cell(unit.getLocation()));
        return list.toArray(new Cell[0]);
    }
}
