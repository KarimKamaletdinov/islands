package com.agrogames.islandsofwar.types;

public enum TextureBitmap {
    Tank("tank.png"),
    TankSelected("tank-selected.png"),
    TankDestroyed("tank-destroyed.png"),
    TankTower("tank-tower.png"),
    TankBullet("tank-bullet.png"),
    RocketLauncher("rocket-launcher.png"),
    RocketLauncherSelected("rocket-launcher-selected.png"),
    RocketLauncherDestroyed("rocket-launcher-destroyed.png"),
    RocketLauncherTower("rocket-launcher-tower.png"),
    Rocket("rocket.png"),
    Error("error.png"),
    Red("red.png"),
    Green("green.png"),
    Pit("pit.png"),
    Bang("bang.png"),
    LandingCraft("landing-craft.png"),
    LandingCraftSelected("landing-craft-selected.png"),
    LandingCraftDestroyed("landing-craft-destroyed.png"),
    Background("background.png"),
    CancelButton("cancel-button.png"),
    LandUnitsButton("land-units-button.png"),
    LandUnitsButtonSelected("land-units-button-selected.png"),
    ButtonBackground("button-background.png"),
    TransportShip("transport-ship.png"),
    TransportShipSelected("transport-ship-selected.png"),
    TransportShipDestroyed("transport-ship-destroyed.png"),
    TransportShipTower("transport-ship-tower.png"),
    TransportShipBullet("transport-ship-bullet.png");

    public final String name;

    TextureBitmap(String name){
        this.name = name;
    }
}
