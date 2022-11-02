package com.agrogames.islandsofwar.engine.impl.another;

import com.agrogames.islandsofwar.engine.abs.another.IGraphicsObject;

import java.util.ArrayList;
import java.util.List;

public class AnotherAdder implements com.agrogames.islandsofwar.engine.abs.another.AnotherAdder {
    private final List<IGraphicsObject> graphicsObjects = new ArrayList<>();
    @Override
    public void addAnother(IGraphicsObject object) {
        graphicsObjects.add(object);
    }

    public List<IGraphicsObject> getAnotherObjects() {
        return graphicsObjects;
    }
}
