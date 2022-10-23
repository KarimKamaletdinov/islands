package com.agrogames.islandsofwar.graphics.abs;

public interface RenderManager {
    void render(TextureDrawer renderer);
    void onTouch(float x, float y);
    void onMove(float x, float y);
}
