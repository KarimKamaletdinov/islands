package com.agrogames.islandsofwar.graphics.abs;

public enum TextureBitmap {
    Tank("tank.png"),
    TankSelected("tank-selected.png"),
    TankTower("tank-tower.png"),
    TankBullet("tank-bullet.png"),
    Error("error.png"),
    Background("background.png");

    public final String name;

    TextureBitmap(String name){
        this.name = name;
    }
}
