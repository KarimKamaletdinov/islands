package com.agrogames.islandsofwar.engine.abs.another;

import com.agrogames.islandsofwar.engine.abs.renderable.RenderableObject;
import com.agrogames.islandsofwar.engine.abs.updatable.UpdatableObject;
import com.agrogames.islandsofwar.types.AnotherObjectType;
import com.agrogames.islandsofwar.types.BulletType;

public interface AnotherObject extends RenderableObject, UpdatableObject {
    boolean shouldBeRemoved();
    AnotherObjectType getType();
}
