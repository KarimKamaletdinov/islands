package com.agrogames.islandsofwar.engine.abs.common;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Point {
    public float x;
    public float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Point(Cell cell) {
        this.x = cell.x - 0.5f;
        this.y = cell.y - 0.5f;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Float.compare(point.x, x) == 0 && Float.compare(point.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public Point rotate(float angle){
        return new Point(
                (float) ((double)x * Math.cos(angle) - (double) y * Math.sin(angle)),
                (float) ((double)x * Math.sin(angle) + (double) y * Math.cos(angle))
        );
    }

    @NonNull
    @Override
    public String toString() {
        return "{" + x + ";" + y + "}";
    }
}
