package com.agrogames.islandsofwar.engine.abs.common;

import androidx.annotation.NonNull;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return x == cell.x && y == cell.y;
    }

    public boolean nearlyEquals(Point p) {
        return p.x + 0.7f >= x && p.x + 0.5f <= x &&
                p.y + 0.7f >= y && p.y + 0.5f <= y;
    }

    @NonNull
    @Override
    public String toString() {
        return "{" + x + ";" + y + "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

