package com.agrogames.islandsofwar.ui.abs;


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
    void setTexture(String texture);
    String getTexture();
    void setVisible(boolean visible);
    boolean getVisible();
    void onClick(Callable<Void> listener);
    boolean callListener();
    void setRenderInBorders(boolean inBorders);
    boolean getRenderInBorders();
}
