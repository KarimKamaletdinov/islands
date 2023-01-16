package com.agrogames.islandsofwar.engine.abs.graphics;

import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObject;
import com.agrogames.islandsofwar.engine.abs.updatable.UpdatableObject;

public interface IGraphicsObject extends RenderableObject, UpdatableObject {
    boolean shouldBeRemoved();
    String getSpawnSound();
}
