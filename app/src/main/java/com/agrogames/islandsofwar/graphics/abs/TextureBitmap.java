package com.agrogames.islandsofwar.graphics.abs;

public enum TextureBitmap {
    Tank("tank.png"),
    TankSelected("tank-selected.png"),
    TankTower("tank-tower.png"),
    TankBullet("tank-bullet.png"),
    Error("error.png"),
    LandingCraft("landing-craft.png"),
    LandingCraftSelected("landing-craft-selected.png"),
    Background("background.png"),
    CancelButton("cancel-button.png"),
    LandUnitsButton("land-units-button.png"),
    LandUnitsButtonSelected("land-units-button-selected.png"),
    TransportShip("transport-ship.png"),
    TransportShipSelected("transport-ship-selected.png"),
    TransportShipTower("transport-ship-tower.png"),
    TransportShipBullet("transport-ship-bullet.png");

    public final String name;

    TextureBitmap(String name){
        this.name = name;
    }
}
