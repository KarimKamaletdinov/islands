package com.agrogames.islandsofwar.islands.impl;

import android.content.Context;

import com.agrogames.islandsofwar.engine.abs.unit.IUnit;
import com.agrogames.islandsofwar.factories.UnitFactory;
import com.agrogames.islandsofwar.islands.abs.Island;
import com.agrogames.islandsofwar.map.impl.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class IslandFactory {
    public static Island parse(Context context, int id, String text) throws JSONException {
        JSONObject json = new JSONObject(text);
        Map map = Map.fromAssets(context, "map" + json.getInt("map_id") + ".txt");
        JSONArray unitsJson = json.getJSONArray("units");
        IUnit[] units = new IUnit[unitsJson.length()];
        for (int i = 0; i < unitsJson.length(); i++){
            JSONObject unit = unitsJson.getJSONObject(i);
            if(unit.has("content")){
                JSONArray contentJson = unit.getJSONArray("content");
                String[] content = new String[contentJson.length()];
                for (int j = 0; j < contentJson.length(); j++) {
                    content[j] = contentJson.getString(j);
                }
                units[i]  = UnitFactory.get(
                        unit.getString("name"),
                        unit.getInt("x"),
                        unit.getInt("y"),
                        content);
            } else {
                units[i]  = UnitFactory.get(
                        unit.getString("name"),
                        unit.getInt("x"),
                        unit.getInt("y"));
            }
            if(unit.has("health")){
                units[i].loseHealth(units[i].getHealth().start - unit.getInt("health"));
            }
        }
        JSONObject a = json.getJSONObject("ship");
        JSONArray contentJson = a.getJSONArray("content");
        String[] content = new String[contentJson.length()];
        for (int j = 0; j < contentJson.length(); j++) {
            content[j] = contentJson.getString(j);
        }
        IUnit attacker  = UnitFactory.get(
                a.getString("name"),
                1,
                1,
                content);
        return new Island(id, map, units, attacker);
    }
}
