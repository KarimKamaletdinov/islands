package com.agrogames.islandsofwar.render.abs;

import com.agrogames.islandsofwar.graphics.abs.TextureDrawer;

public interface Renderer {
    void render(TextureDrawer drawer);
    void onTouch(float x, float y);
}
