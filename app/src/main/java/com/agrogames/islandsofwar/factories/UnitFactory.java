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
import com.agrogames.islandsofwar.engine.impl.another.GraphicsObject;
import com.agrogames.islandsofwar.engine.impl.unit.BigShip;
import com.agrogames.islandsofwar.engine.impl.unit.LandUnit;
import com.agrogames.islandsofwar.engine.impl.unit.Plane;
import com.agrogames.islandsofwar.engine.impl.unit.SmallShip;
import com.agrogames.islandsofwar.engine.impl.weapon.Weapon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class UnitFactory {
    private static final List<JSONObject> loaded = new ArrayList<>();

    public static void load(Context context){
        try {
            String text = new Scanner(context.getAssets().open("units.json")).useDelimiter("\\A").next();
            JSONArray json = new JSONArray(text);
            for (int i = 0; i < json.length(); i++){
                JSONObject unit = json.getJSONObject(i);
                loaded.add(unit);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("IOW","Unable to load ", e);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("IOW","Error in JSON syntax", e);
        }
    }

    public static IUnit byTexture(String texture, int x, int y, String... contentTextures){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            JSONObject[] content = Arrays.stream(contentTextures).map(UnitFactory::jsonByTexture).toArray(JSONObject[]::new);
            try {
                return unit(jsonByTexture(texture), new Cell(x, y), content);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("IOW","Error in JSON schema", e);
            }
        }
        return new LandUnit("ERROR", new Cell(-1, -1), new IWeapon[0], 0, 0, 0);
    }

    public static IGraphicsObject graphicsByTexture(String texture, Point location, float rotation){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            try {
                return graphics(jsonByTexture(texture), location, rotation);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("IOW","Error in JSON schema", e);
            }
        }
        return new GraphicsObject("ERROR", new Point(-1, -1), 0, 0);
    }

    private static JSONObject jsonByTexture(String texture){
        try {
            for (JSONObject unit : loaded){
                if(unit.get("texture") == texture){
                    return unit;
                }
            }
            Log.e("IOW","Unit with texture " + texture + " NOT FOUND");
            return new JSONObject();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("IOW","Error in JSON syntax", e);
            return new JSONObject();
        }
    }

    private static IGraphicsObject graphics(JSONObject json, Point location, float rotation) throws JSONException{
        if(json.has("next")){
            return new GraphicsObject(json.getString("texture"), location,
                    rotation,
                    graphicsByTexture(json.getString("next"), location, rotation),
                    (float) json.getDouble("life_time"));
        }
        return new GraphicsObject(json.getString("texture"), location,
                rotation,
                (float) json.getDouble("life_time"));
    }

    private static IUnit unit(JSONObject json, Cell location, JSONObject[] content) throws JSONException {
        switch (json.getString("type")){
            case "land":
                return new LandUnit(
                        json.getString("texture"),
                        location,
                        weapons(json.getJSONArray("weapons")),
                        json.getInt("health"),
                        (float) json.getDouble("speed"),
                        (float) json.getDouble("rotation_speed")
                );
            case "bigShip":
                return new BigShip(
                        json.getString("texture"),
                        location,
                        weapons(json.getJSONArray("weapons")),
                        json.getInt("health"),
                        (float) json.getDouble("speed"),
                        (float) json.getDouble("rotation_speed"),
                        transportUnits(content),
                        json.getInt("min_damage")
                );
            case "smallShip":
                return new SmallShip(
                        json.getString("texture"),
                        location,
                        weapons(json.getJSONArray("weapons")),
                        json.getInt("health"),
                        (float) json.getDouble("speed"),
                        (float) json.getDouble("rotation_speed")
                );
            case "plane":
                JSONObject bomb = json.getJSONObject("bomb");
                return new Plane(
                        json.getString("texture"),
                        json.getInt("health"),
                        (float) json.getDouble("speed"),
                        (float) json.getDouble("rotation_speed"),
                        json.getInt("bomb_count"),
                        bomb.getString("texture"),
                        bomb.getInt("power")
                );
            default:
                throw new JSONException("Unknown type of Unit: " + json.getString("type"));
        }
    }

    private static IWeapon[] weapons(JSONArray json) throws JSONException {
        IWeapon[] result = new IWeapon[json.length()];
        for (int i = 0; i < json.length(); i++){
            result[i] = weapon(json.getJSONObject(i));
        }
        return result;
    }

    private static IWeapon weapon(JSONObject json) throws JSONException{
        JSONObject bullet = json.getJSONObject("bullet");
        return new Weapon(
                point(json.getJSONObject("location")),
                (float)json.getDouble("reload"),
                json.getString("texture"),
                (float)json.getDouble("range"),
                (float)json.getDouble("rotation_speed"),
                points(json.getJSONArray("bullet_starts")), bullet.getString("texture"), bullet.getInt("damage"),
                (float) bullet.getDouble("speed"),
                bullet.getInt("flight_height"),
                bullet.getInt("target_height"),
                bullet.getBoolean("bang")
        );
    }

    private static Point[] points(JSONArray json) throws JSONException{
        Point[] result = new Point[json.length()];
        for (int i = 0; i < json.length(); i++){
            result[i] = point(json.getJSONObject(i));
        }
        return result;
    }

    private static Point point(JSONObject json) throws JSONException{
        return new Point(json.getInt("x"), json.getInt("y"));
    }

    private static TransportUnit[] transportUnits(JSONObject[] json) {
        TransportUnit[] result = new TransportUnit[json.length];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            for (int i = 0; i < json.length; i++){
                result[i] = transportUnit(json[i]);
            }
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static TransportUnit transportUnit(JSONObject json) {
        return new TransportUnit(cell -> {
            try {
                return unit(json, cell, new JSONObject[0]);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        });
    }
}
