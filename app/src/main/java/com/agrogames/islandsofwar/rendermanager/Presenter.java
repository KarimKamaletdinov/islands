package com.agrogames.islandsofwar.rendermanager;

import com.agrogames.islandsofwar.engine.abs.Engine;
import com.agrogames.islandsofwar.engine.abs.GameState;
import com.agrogames.islandsofwar.engine.abs.bullet.IBullet;
import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObject;
import com.agrogames.islandsofwar.engine.abs.unit.IUnit;

public class Presenter implements com.agrogames.islandsofwar.render.abs.Presenter {
    private final Engine engine;

    public Presenter(Engine engine) {
        this.engine = engine;
    }

    @Override
    public IUnit[] getProtectors() {
        return engine.getProtectors();
    }

    @Override
    public IUnit[] getAttackers() {
        return engine.getAttackers();
    }

    @Override
    public RenderableObject[] getOther() {
        return engine.getOther();
    }

    @Override
    public IBullet[] getProtectorsBullets() {
        return engine.getProtectorsBullets();
    }

    @Override
    public IBullet[] getAttackersBullets() {
        return engine.getAttackersBullets();
    }

    @Override
    public GameState getState() {
        return engine.getState();
    }

    @Override
    public void addPlane(IUnit plane){
        engine.addPlane(plane);
    }
}
