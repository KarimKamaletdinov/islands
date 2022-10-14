package com.agrogames.islandsofwar.ui.abs;

import com.agrogames.islandsofwar.graphics.abs.TextureBitmap;

import java.util.concurrent.Callable;

public interface Element {
    void setType(ElementType type);
    ElementType getType();
    void setX(float x);
    float getX();
    void setY(float y);
    float getY();
    void setWidth(float width);
    float getWidth();
    void setHeight(float height);
    float getHeight();
    void setTexture(TextureBitmap texture);
    TextureBitmap getTexture();
    void setVisible(boolean visible);
    boolean getVisible();
    void onClick(Callable<Void> listener);
    void callListener();
}
