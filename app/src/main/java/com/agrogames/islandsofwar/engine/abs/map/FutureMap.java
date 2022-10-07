package com.agrogames.islandsofwar.engine.abs.map;

public interface FutureMap extends Map {
    boolean isTaken(int x, int y, int tick);
}
