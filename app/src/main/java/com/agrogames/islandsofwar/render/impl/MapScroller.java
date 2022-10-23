package com.agrogames.islandsofwar.render.impl;

import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.graphics.abs.TextureDrawer;
import com.agrogames.islandsofwar.map.abs.MapParams;

public class MapScroller {
    private static float currentX;
    private static float currentY;

    public static void start(TextureDrawer drawer){
        drawer.translate(currentX, currentY);
    }

    public static void finish(TextureDrawer drawer){
        drawer.translate(-currentX, -currentY);
    }

    public static Point convert(Point point){
        return new Point(point.x - currentX, point.y - currentY);
    }

    public static void scroll(float x, float y){
        currentX += x;
        currentY += y;

        if(currentX > 0) currentX = 0;
        if(currentY > 0) currentY = 0;
        if(currentX < -MapParams.Width + 15) currentX = -MapParams.Width + 15;
        if(currentY < -MapParams.Height + 10) currentY = -MapParams.Height + 10;
    }
}
