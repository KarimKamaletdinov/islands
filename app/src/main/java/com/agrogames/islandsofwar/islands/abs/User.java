package com.agrogames.islandsofwar.islands.abs;

public class User {
    public final String name;
    public final String shipName;
    public final String[] shipContent;

    public User(String name, String shipName, String[] shipContent) {
        this.name = name;
        this.shipName = shipName;
        this.shipContent = shipContent;
    }
}
