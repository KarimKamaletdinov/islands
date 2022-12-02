package com.agrogames.islandsofwar.common;

import com.agrogames.islandsofwar.engine.abs.common.Point;

public class M {
    public static int module(int a){
        return a >= 0 ? a : -a;
    }

    public static float module(float a){
        return a >= 0 ? a : -a;
    }

    public static double module(double a){
        return a >= 0 ? a : -a;
    }

    public static boolean nearlyEquals(float a, float b){
        return module(a - b) < 0.0001f;
    }
    public static float sqrt(float a){
        return (float) Math.sqrt(a);
    }

    public static float dist(Point a, Point b){
        float w = module(a.x - b.x);
        float h = module(a.y) - b.y;
        return sqrt(w * w + h * h);
    }

    public static Point middle(Point a, Point b){
        return new Point((a.x + b.x) / 2f, (a.y + b.y) / 2f);
    }
}
