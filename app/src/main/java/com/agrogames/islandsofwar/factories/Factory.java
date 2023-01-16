package com.agrogames.islandsofwar.factories;

import android.content.Context;
import android.util.Log;

import com.agrogames.islandsofwar.engine.abs.graphics.IGraphicsObject;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.unit.IUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Scanner;

public class Factory {

    public static void load(Context context){
        try {
            String text = new Scanner(context.getAssets().open("units.json")).useDelimiter("\\A").next();
            text = text.replace("\\/", "/");
            JSONObject json = new JSONObject(text);

            JSONArray u = json.getJSONArray("units");
            UnitFactory.units.clear();
            for (int i = 0; i < u.length(); i++){
                JSONObject unit = u.getJSONObject(i);
                UnitFactory.units.add(unit);
            }

            JSONArray w = json.getJSONArray("weapons");
            WeaponFactory.weapons.clear();
            for (int i = 0; i < w.length(); i++){
                JSONObject weapon = w.getJSONObject(i);
                WeaponFactory.weapons.add(weapon);
            }

            JSONArray g = json.getJSONArray("graphics");
            GraphicsFactory.graphics.clear();
            for (int i = 0; i < g.length(); i++){
                JSONObject gr = g.getJSONObject(i);
                GraphicsFactory.graphics.add(gr);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("IOW","Unable to load ", e);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("IOW","Error in JSON syntax", e);
        }
    }

    public static IUnit get(String name, int x, int y, String... contentTextures){
        return UnitFactory.get(name, x, y, contentTextures);
    }

    public static IGraphicsObject getGraphics(String name, Point location, float rotation){
        return GraphicsFactory.getGraphics(name, location, rotation);
    }
}
