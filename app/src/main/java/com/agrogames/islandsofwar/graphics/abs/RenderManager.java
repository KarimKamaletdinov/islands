package com.agrogames.islandsofwar.graphics.abs;

public interface RenderManager {
    void render(TextureDrawer renderer);
    void onTouch(float x, float y);
    void onMove(float x, float y);
    void onZoom(float x1, float y1, float x2, float y2);
    void stop();
}
