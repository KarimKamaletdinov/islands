package com.agrogames.islandsofwar.ui.impl;

import android.util.Log;

import com.agrogames.islandsofwar.ui.abs.ElementType;

import java.util.concurrent.Callable;

public class Element implements com.agrogames.islandsofwar.ui.abs.Element {
    private ElementType type;
    private float x;
    private float y;
    private float width;
    private float height;
    private String texture;
    private Callable<Void> listener;
    private boolean visible;
    private boolean renderInBorders;

    @Override
    public void setType(ElementType type) {
        this.type = type;
    }

    @Override
    public ElementType getType() {
        return type;
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void setWidth(float width) {
        this.width = width;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public void setHeight(float height) {
        this.height = height;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void setTexture(String texture) {
        this.texture = texture;
    }

    @Override
    public String getTexture() {
        return texture;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean getVisible() {
        return visible;
    }

    @Override
    public void onClick(Callable<Void> listener) {
        this.listener = listener;
    }

    @Override
    public boolean callListener() {
        if(listener == null) return false;
        try {
            listener.call();
        } catch (Exception e) {
            Log.e("IOW", "Exception when calling listener from UI element", e);
        }
        return true;
    }

    @Override
    public void setRenderInBorders(boolean inBorders) {
        renderInBorders = inBorders;
    }

    @Override
    public boolean getRenderInBorders() {
        return renderInBorders;
    }
}
