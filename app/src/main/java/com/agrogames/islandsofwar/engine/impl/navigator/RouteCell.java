package com.agrogames.islandsofwar.engine.impl.navigator;

import com.agrogames.islandsofwar.engine.abs.common.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RouteCell {
    public final Cell cell;
    public final List<Direction> triedDirections = new ArrayList<>();

    public RouteCell(Cell cell) {
        this.cell = cell;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouteCell routeCell = (RouteCell) o;
        return Objects.equals(cell, routeCell.cell);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cell);
    }
}
