package com.agrogames.islandsofwar.ui.abs;

import com.agrogames.islandsofwar.types.TextureBitmap;
import com.agrogames.islandsofwar.graphics.abs.TextureDrawer;

public interface UI {
    Element createElement(ElementType type, float x, float y, float width, float height, TextureBitmap texture);
    void deleteElement(Element element);
    boolean render(TextureDrawer drawer, Float touchX, Float touchY);
}
