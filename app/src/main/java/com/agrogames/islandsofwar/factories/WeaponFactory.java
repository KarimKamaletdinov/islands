package com.agrogames.islandsofwar.factories;

import android.util.Log;

import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.weapon.IWeapon;
import com.agrogames.islandsofwar.engine.impl.weapon.Weapon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeaponFactory {
    public static final List<JSONObject> weapons = new ArrayList<>();

    public static JSONObject getJSONWeapon(String name){
        try {
            for (JSONObject w : weapons){
                if(w.getString("name").equals(name)){
                    return w;
                }
            }
            Log.e("IOW","Weapon with name " + name + " NOT FOUND");
            return new JSONObject();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("IOW","Error in JSON syntax", e);
            return new JSONObject();
        }
    }

    public static IWeapon getWeapon(String name, Point location){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            try {
                return weapon(getJSONWeapon(name), location);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("IOW","Error in JSON schema", e);
            }
        }
        return new Weapon(new Point(-1, -1), 0, "ERROR", 0, 0, new Point[0], "ERROR", 0, 0, 0, 0, false);
    }

    public static IWeapon[] weapons(JSONArray json) throws JSONException {
        IWeapon[] result = new IWeapon[json.length()];
        for (int i = 0; i < json.length(); i++){
            Object o = json.get(i);
            if(o instanceof String){
                result[i] = getWeapon((String) o, new Point(0, 0));
            } else if(o instanceof JSONObject){
                JSONObject jo = (JSONObject) o;
                result[i] = getWeapon(jo.getString("name"), PointFactory.point(jo.getJSONObject("location")));
            }
        }
        return result;
    }

    private static IWeapon weapon(JSONObject json, Point location) throws JSONException{
        JSONObject bullet = json.getJSONObject("bullet");
        return new Weapon(
                location,
                (float)json.getDouble("reload"),
                "weapons/" + (json.has("texture") ? json.getString("texture") : json.getString("name")),
                (float)json.getDouble("range"),
                (float)json.getDouble("rotation_speed"),
                PointFactory.points(json.getJSONArray("bullet_starts")),
                "bullets/" + (bullet.has("texture") ? bullet.getString("texture") : bullet.getString("name")),
                bullet.getInt("damage"),
                (float) bullet.getDouble("speed"),
                bullet.getInt("flight_height"),
                bullet.getInt("target_height"),
                bullet.getBoolean("bang")
        );
    }
}
