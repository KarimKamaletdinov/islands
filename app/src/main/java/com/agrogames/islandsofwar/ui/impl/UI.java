package com.agrogames.islandsofwar.ui.impl;

import com.agrogames.islandsofwar.graphics.abs.TextureBitmap;
import com.agrogames.islandsofwar.graphics.abs.TextureDrawer;
import com.agrogames.islandsofwar.ui.abs.Element;
import com.agrogames.islandsofwar.ui.abs.ElementType;

import java.util.ArrayList;
import java.util.List;

public class UI implements com.agrogames.islandsofwar.ui.abs.UI {
    private final List<Element> elements = new ArrayList<>();
    @Override
    public Element createElement(ElementType type, float x, float y, float width, float height, TextureBitmap texture) {
        Element element = new com.agrogames.islandsofwar.ui.impl.Element();
        element.setType(type);
        element.setX(x);
        element.setY(y);
        element.setWidth(width);
        element.setHeight(height);
        element.setTexture(texture);
        element.setVisible(true);
        elements.add(element);
        return element;
    }

    @Override
    public void render(TextureDrawer drawer, Float touchX, Float touchY) {
        for (Element element : elements){
            if(!element.getVisible()) continue;
            drawer.DrawTexture(element.getX(), element.getY(), element.getTexture(),
                    element.getWidth(), element.getHeight(), 0);
            if(touchX != null && touchY != null){
                if(element.getX() + element.getWidth() / 2 > touchX
                && element.getX() - element.getWidth() / 2 < touchX
                && element.getY() + element.getHeight() / 2 > touchY
                && element.getY() - element.getHeight() / 2 < touchY){
                    element.callListener();
                }
            }
        }
    }
}
