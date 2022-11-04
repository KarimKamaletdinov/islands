package com.agrogames.islandsofwar.factories;

import android.util.Log;

import com.agrogames.islandsofwar.engine.abs.another.IGraphicsObject;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.impl.another.GraphicsObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GraphicsFactory {

    public static final List<JSONObject> graphics = new ArrayList<>();
    public static IGraphicsObject getGraphics(String name, Point location, float rotation){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            try {
                return graphics(getJSONGraphics(name), location, rotation);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("IOW","Error in JSON schema", e);
            }
        }
        return new GraphicsObject("ERROR", new Point(-1, -1), 0, 0);
    }

    private static JSONObject getJSONGraphics(String name){
        try {
            for (JSONObject g : graphics){
                if(g.getString("name").equals(name)){
                    return g;
                }
            }
            Log.e("IOW","Graphics object with name " + name + " NOT FOUND");
            return new JSONObject();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("IOW","Error in JSON syntax", e);
            return new JSONObject();
        }
    }

    private static IGraphicsObject graphics(JSONObject json, Point location, float rotation) throws JSONException{
        if(json.has("next")){
            return new GraphicsObject("graphics/" + json.getString("name"), location,
                    rotation,
                    getGraphics(json.getString("next"), location, rotation),
                    (float) json.getDouble("lifetime"));
        }
        return new GraphicsObject("graphics/" + json.getString("name"), location,
                rotation,
                (float) json.getDouble("lifetime"));
    }
}
