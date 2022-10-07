package com.agrogames.islandsofwar.engine.abs.common;

public class Cell {
    public final int x;
    public final int y;

    public Cell(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Cell(Point point){
        this.x = (int) (point.x + 0.5f);
        this.y = (int) (point.y + 0.5f);
    }
}


