package com.agrogames.islandsofwar.engine.abs.renderable;

import com.agrogames.islandsofwar.engine.abs.common.Point;

public interface RenderableObject {
    Point getLocation();
    float getRotation();
    String getTexture();
    default float timeSinceDestroyed(){
        return 0;
    }
}
