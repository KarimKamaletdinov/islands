package com.agrogames.islandsofwar.engine.impl.another;

import com.agrogames.islandsofwar.engine.abs.another.AnotherObject;

import java.util.ArrayList;
import java.util.List;

public class AnotherAdder implements com.agrogames.islandsofwar.engine.abs.another.AnotherAdder {
    private final List<AnotherObject> anotherObjects = new ArrayList<>();
    @Override
    public void addAnother(AnotherObject object) {
        anotherObjects.add(object);
    }

    public List<AnotherObject> getAnotherObjects() {
        return anotherObjects;
    }
}
