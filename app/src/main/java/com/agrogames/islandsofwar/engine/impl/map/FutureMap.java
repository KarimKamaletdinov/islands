package com.agrogames.islandsofwar.engine.impl.map;

class FutureMap implements com.agrogames.islandsofwar.engine.abs.map.FutureMap {
    private final boolean[][][] map;

    FutureMap(boolean[][][] map) {
        this.map = map;
    }

    @Override
    public boolean isTaken(int x, int y, int tick) {
        return map[x][y][tick];
    }

    @Override
    public boolean isTaken(int x, int y) {
        return isTaken(x, y, 0);
    }
}
