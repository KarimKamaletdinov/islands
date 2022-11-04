package com.agrogames.islandsofwar.factories;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.agrogames.islandsofwar.engine.abs.another.IGraphicsObject;
import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.transport.TransportUnit;
import com.agrogames.islandsofwar.engine.abs.unit.IUnit;
import com.agrogames.islandsofwar.engine.abs.weapon.IWeapon;
import com.agrogames.islandsofwar.engine.impl.unit.BigShip;
import com.agrogames.islandsofwar.engine.impl.unit.LandUnit;
import com.agrogames.islandsofwar.engine.impl.unit.Plane;
import com.agrogames.islandsofwar.engine.impl.unit.SmallShip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Factory {

    public static void load(Context context){
        try {
            String text = new Scanner(context.getAssets().open("units.json")).useDelimiter("\\A").next();
            text = text.replace("\\/", "/");
            JSONObject json = new JSONObject(text);

            JSONArray u = json.getJSONArray("units");
            for (int i = 0; i < u.length(); i++){
                JSONObject unit = u.getJSONObject(i);
                UnitFactory.units.add(unit);
            }

            JSONArray w = json.getJSONArray("weapons");
            for (int i = 0; i < w.length(); i++){
                JSONObject weapon = w.getJSONObject(i);
                WeaponFactory.weapons.add(weapon);
            }

            JSONArray g = json.getJSONArray("graphics");
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
