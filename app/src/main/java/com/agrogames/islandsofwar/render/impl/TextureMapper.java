package com.agrogames.islandsofwar.render.impl;

public class TextureMapper {
    public static String join(String texture){
        return join(texture, "normal");
    }

    public static String join(String texture, String state){
        return texture + "/" + state;
    }
}
