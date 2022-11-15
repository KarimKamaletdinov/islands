package com.agrogames.islandsofwar.engine.impl.graphics;

import com.agrogames.islandsofwar.engine.abs.graphics.IGraphicsObject;
import com.agrogames.islandsofwar.sounds.abs.SoundPlayer;

import java.util.ArrayList;
import java.util.List;

public class GraphicsAdder implements com.agrogames.islandsofwar.engine.abs.graphics.GraphicsAdder {
    private final List<IGraphicsObject> graphicsObjects = new ArrayList<>();
    private final SoundPlayer soundPlayer;

    public GraphicsAdder(SoundPlayer soundPlayer) {
        this.soundPlayer = soundPlayer;
    }

    @Override
    public void addGraphics(IGraphicsObject object) {
        graphicsObjects.add(object);
        if(object.getSpawnSound() != null) {
            soundPlayer.playSound(object.getSpawnSound());
        }
    }

    public List<IGraphicsObject> getAnotherObjects() {
        return graphicsObjects;
    }
}
