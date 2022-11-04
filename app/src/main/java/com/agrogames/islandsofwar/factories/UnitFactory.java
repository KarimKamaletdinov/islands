package com.agrogames.islandsofwar.factories;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.transport.TransportUnit;
import com.agrogames.islandsofwar.engine.abs.unit.IUnit;
import com.agrogames.islandsofwar.engine.abs.weapon.IWeapon;
import com.agrogames.islandsofwar.engine.impl.unit.BigShip;
import com.agrogames.islandsofwar.engine.impl.unit.Building;
import com.agrogames.islandsofwar.engine.impl.unit.LandUnit;
import com.agrogames.islandsofwar.engine.impl.unit.Plane;
import com.agrogames.islandsofwar.engine.impl.unit.SmallShip;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnitFactory {
    public static final List<JSONObject> units = new ArrayList<>();

    public static IUnit get(String name, int x, int y, String... contentTextures){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            JSONObject[] content = Arrays.stream(contentTextures).map(UnitFactory::getJSONUnit).toArray(JSONObject[]::new);
            try {
                return unit(getJSONUnit(name), new Cell(x, y), content);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("IOW","Error in JSON schema", e);
            }
        }
        return new LandUnit("ERROR", new Cell(-1, -1), new IWeapon[0], 0, 0, 0);
    }

    private static IUnit unit(JSONObject json, Cell location, JSONObject[] content) throws JSONException {
        switch (json.getString("type")){
            case "land":
                return new LandUnit(
                        "units/" + (json.has("texture") ? json.getString("texture") : json.getString("name")),
                        location,
                        WeaponFactory.weapons(json.getJSONArray("weapons")),
                        json.getInt("health"),
                        (float) json.getDouble("speed"),
                        (float) json.getDouble("rotation_speed")
                );
            case "building":
                return new Building(
                        "units/" + (json.has("texture") ? json.getString("texture") : json.getString("name")),
                        location,
                        WeaponFactory.weapons(json.getJSONArray("weapons")),
                        json.getInt("health")
                );
            case "big_ship":
                return new BigShip(
                        "units/" + (json.has("texture") ? json.getString("texture") : json.getString("name")),
                        location,
                        WeaponFactory.weapons(json.getJSONArray("weapons")),
                        json.getInt("health"),
                        (float) json.getDouble("speed"),
                        (float) json.getDouble("rotation_speed"),
                        transportUnits(content),
                        json.getInt("min_damage")
                );
            case "small_ship":
                return new SmallShip(
                        "units/" + (json.has("texture") ? json.getString("texture") : json.getString("name")),
                        location,
                        WeaponFactory.weapons(json.getJSONArray("weapons")),
                        json.getInt("health"),
                        (float) json.getDouble("speed"),
                        (float) json.getDouble("rotation_speed")
                );
            case "plane":
                JSONObject bomb = json.getJSONObject("bomb");
                return new Plane(
                        "units/" + (json.has("texture") ? json.getString("texture") : json.getString("name")),
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

    private static JSONObject getJSONUnit(String name){
        try {
            for (JSONObject unit : units){
                if(unit.getString("name").equals(name)){
                    return unit;
                }
            }
            Log.e("IOW","Unit with name " + name + " NOT FOUND");
            return new JSONObject();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("IOW","Error in JSON syntax", e);
            return new JSONObject();
        }
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
