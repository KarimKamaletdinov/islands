package com.agrogames.islandsofwar.map.impl;

import android.content.Context;
import android.util.Log;

import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.map.MapObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Map implements com.agrogames.islandsofwar.map.abs.Map {
    private final MapObject[] map;

    private Map(MapObject[] map) {
        this.map = map;
    }

    public static Map fromAssets(Context context, String name){
        try {
            return load(new Scanner(context.getAssets().open("maps/" + name)).useDelimiter("\\A").next());
        } catch (IOException e){
            Log.e("IOW", "Cannot load map with name " + name, e);
            return new Map(new MapObject[0]);
        }
    }

    private static Map load(String fileContent){
        List<MapObject> map = new ArrayList<>();
        String[] rows = fileContent.split("\n");
        for (int i = 0; i < rows.length; i++){
            String row = rows[i];
            for(int j = 0; j < row.length(); j++){
                char symbol = row.charAt(j);
                if(symbol == 'w'){
                    map.add(new Water(new Cell(j, 10-i)));
                }
            }
        }
        return new Map(map.toArray(new MapObject[0]));
    }

    @Override
    public MapObject[] getMap() {
        return map;
    }
}
