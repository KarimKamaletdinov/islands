package com.agrogames.islandsofwar.rendermanager;

import com.agrogames.islandsofwar.engine.abs.Engine;
import com.agrogames.islandsofwar.engine.abs.game.Bullet;
import com.agrogames.islandsofwar.engine.abs.game.GameObject;

public class Presenter implements com.agrogames.islandsofwar.render.abs.Presenter {
    private final Engine engine;

    public Presenter(Engine engine) {
        this.engine = engine;
    }

    @Override
    public GameObject[] getProtectors() {
        return engine.getProtectors();
    }

    @Override
    public GameObject[] getAttackers() {
        return engine.getAttackers();
    }

    @Override
    public Bullet[] getProtectorsBullets() {
        return engine.getProtectorsBullets();
    }

    @Override
    public Bullet[] getAttackersBullets() {
        return engine.getAttackersBullets();
    }
}
