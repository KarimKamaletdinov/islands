package com.agrogames.islandsofwar.render.abs;

import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.graphics.abs.TextureDrawer;

public interface Renderer {
    void render(TextureDrawer drawer, Point touchPoint, Point movePoint, Point previousMovePoint, Point zoom1, Point zoom2, Point previousZoom1, Point previousZoom2);
}
