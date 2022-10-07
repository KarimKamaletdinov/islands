package com.agrogames.islandsofwar.engine.abs.gamevalue;

public class GameValue<T> {
    public final T start;
    public T current;

    public GameValue(T start) {
        this.start = start;
        current = start;
    }
}
