package com.agrogames.islandsofwar.common;

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
}
