package com.agrogames.islandsofwar.render.impl;

import com.agrogames.islandsofwar.common.M;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.graphics.abs.TextureDrawer;
import com.agrogames.islandsofwar.map.abs.MapParams;

public class MapScroller {
    private static float currentX;
    private static float currentY;
    private static float currentZoom = 1;

    public static void start(TextureDrawer drawer){
        drawer.translate(currentX, currentY);
        drawer.scale(currentZoom);
    }

    public static void finish(TextureDrawer drawer){
        drawer.translate(-currentX, -currentY);
        drawer.scale(1 / currentZoom);
    }

    public static Point convert(Point point){
        return new Point(point.x / currentZoom - currentX, point.y / currentZoom - currentY);
    }

    public static void scroll(float x, float y){
        currentX += x;
        currentY += y;

        if(currentX > 0) currentX = 0;
        if(currentY > 0) currentY = 0;
        if(currentX < -MapParams.Width + 15) currentX = -MapParams.Width + 15;
        if(currentY < -MapParams.Height + 10) currentY = -MapParams.Height + 10;
    }

    public static void zoom(Point zoom1, Point zoom2, Point previousZoom1, Point previousZoom2) {
        float diff = M.dist(zoom1, zoom2) - M.dist(previousZoom1, previousZoom2);
        if(Float.isNaN(diff)) diff = 0;
        float oldZoom = currentZoom;
        currentZoom += diff / 5f;
        if(currentZoom > 1) currentZoom = 1;
        if(currentZoom < 0.5f) currentZoom = 0.5f;

        Point middle = M.middle(zoom1, zoom2);
        currentX = -(-currentX + middle.x / oldZoom - middle.x / currentZoom);
        currentY = -(-currentY + middle.y / oldZoom - middle.y / currentZoom);
        if(currentX > 0) currentX = 0;
        if(currentY > 0) currentY = 0;
        if(currentX < -MapParams.Width + 15) currentX = -MapParams.Width + 15;
        if(currentY < -MapParams.Height + 10) currentY = -MapParams.Height + 10;
    }
}
